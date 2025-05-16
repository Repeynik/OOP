package org.example;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aggregator {
    private static final Logger logger = Logger.getLogger(Aggregator.class.getName());
    private final int port;
    private final Map<String, NodeInfo> nodes = new ConcurrentHashMap<>();
    private final List<String> wal = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean shutdown = false;
    private final UUID aggregatorId;
    private final File walFile;
    private final List<String> seedAggregatorAddresses;
    private final String myIp;
    private final Set<String> ackedOperations = ConcurrentHashMap.newKeySet();
    private static final int NEIGHBOR_COUNT = 2;
    private final Random random = new Random();
    private List<String> currentNeighbors = new ArrayList<>();

    private final Map<String, Integer> missedHeartbeats = new ConcurrentHashMap<>(); 
    private final Set<String> liveAggregators = ConcurrentHashMap.newKeySet();
    private static final int HEARTBEAT_THRESHOLD = 3;

    private volatile boolean readOnlyMode = false;

    private final Set<Integer> allNumbers = ConcurrentHashMap.newKeySet();
    private final Map<String, List<int[]>> nodeAssignedRanges = new ConcurrentHashMap<>();

    private void assignRangeToNode(String nodeId, int start, int end) {
        nodeAssignedRanges.putIfAbsent(nodeId, new ArrayList<>());
        nodeAssignedRanges.get(nodeId).add(new int[]{start, end});
    }

    private boolean loadWALSafe() {
        try {
            loadWAL();
            return true;
        } catch (Exception e) {
            logger.severe("[WAL] Ошибка чтения WAL: " + e.getMessage());
            return false;
        }
    }

    private void requestReplicaFromCluster() {
        boolean gotReplica = false;
        for (String addr : seedAggregatorAddresses) {
            String[] parts = addr.split(":");
            String ip = parts[0];
            int p = Integer.parseInt(parts[1]);
            if (ip.equals(getMyIp()) && p == port) continue;
            try (Socket s = new Socket(ip, p);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                out.write("GOSSIP_REQUEST;" + aggregatorId + "\n");
                out.flush();
                String line = in.readLine();
                if (line != null && line.startsWith("GOSSIP_REPLY;")) {
                    String replica = line.substring("GOSSIP_REPLY;".length());
                    mergeReplicaTable(replica);
                    gotReplica = true;
                    logger.info("[RECOVERY] Получена реплика от " + addr);
                }
            } catch (IOException ignored) {}
        }
        if (!gotReplica) {
            logger.warning("[RECOVERY] Нет связи с кластером, переходим в режим только чтение");
            readOnlyMode = true;
        } else {
            readOnlyMode = false;
            logger.info("[RECOVERY] Репликация завершена, режим записи восстановлен");
        }
    }

    public Aggregator(String myIp, int port, List<String> seedAggregatorAddresses) {
        this.port = port;
        this.aggregatorId = UUID.randomUUID();
        this.walFile = new File("aggregator_" + aggregatorId + ".wal");
        this.seedAggregatorAddresses = new ArrayList<>(seedAggregatorAddresses);
        this.myIp = myIp;
        boolean walOk = loadWALSafe();
        if (!walOk) {
            logger.severe("[WAL] WAL поврежден, запрашиваем реплику у кластера...");
            requestReplicaFromCluster();
        } else {
            requestReplicaFromCluster();
        }
        startHeartbeat();
        startGossip();
        startHeartbeatMonitor();
    }

    private synchronized void writeWAL(String record) {
        try (FileWriter fw = new FileWriter(walFile, true)) {
            fw.write(record + "\n");
        } catch (IOException e) {
            logger.severe("WAL write error: " + e.getMessage());
        }
    }

    private boolean checkQuorum() {
        int count = 1;
        for (String ip : liveAggregators) {
            count++;
        }
        int quorumSize = (seedAggregatorAddresses.size() + 1) / 2 + 1;
        return count >= quorumSize;
    }

    private void logAddNode(String nodeId) {
        if (checkQuorum()) {
            writeWAL("ADD_NODE;" + nodeId);
        } else {
            logger.warning("[QUORUM] Недостаточно агрегаторов для ADD_NODE");
        }
    }
    private void logRemoveNode(String nodeId) {
        if (checkQuorum()) {
            writeWAL("REMOVE_NODE;" + nodeId);
        } else {
            logger.warning("[QUORUM] Недостаточно агрегаторов для REMOVE_NODE");
        }
    }
    private void logAssignTask(int number, String nodeId) {
        if (checkQuorum()) {
            writeWAL("ASSIGN_TASK;" + number + ";" + nodeId);
        } else {
            logger.warning("[QUORUM] Недостаточно агрегаторов для ASSIGN_TASK");
        }
    }

    private synchronized void loadWAL() {
        if (!walFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(walFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ALERT;")) {
                    wal.add(line);
                } else if (line.startsWith("ADD_NODE;")) {
                    String nodeId = line.split(";")[1];
                    nodes.putIfAbsent(nodeId, new NodeInfo(nodeId, "unknown", -1, "active"));
                } else if (line.startsWith("REMOVE_NODE;")) {
                    String nodeId = line.split(";")[1];
                    nodes.remove(nodeId);
                } else if (line.startsWith("ASSIGN_TASK;")) {
                    String[] parts = line.split(";");
                    int number = Integer.parseInt(parts[1]);
                    String nodeId = parts[2];
                    allNumbers.add(number);
                } else if (line.startsWith("UNASSIGN_TASK;")) {
                    String[] parts = line.split(";");
                    int number = Integer.parseInt(parts[1]);
                    allNumbers.remove(number);
                }
            }
        } catch (IOException e) {
            logger.severe("WAL load error: " + e.getMessage());
        }
    }

    private void selectRandomNeighbors() {
        List<String> candidates = new ArrayList<>(seedAggregatorAddresses);
        candidates.removeIf(addr -> {
            String[] parts = addr.split(":");
            return parts[0].equals(getMyIp()) && Integer.parseInt(parts[1]) == port;
        });
        Collections.shuffle(candidates, random);
        currentNeighbors = candidates.subList(0, Math.min(NEIGHBOR_COUNT, candidates.size()));
    }

    private void startHeartbeat() {
        new Thread(() -> {
            while (!shutdown) {
                selectRandomNeighbors();
                for (String addr : currentNeighbors) {
                    String[] parts = addr.split(":");
                    String ip = parts[0];
                    int p = Integer.parseInt(parts[1]);
                    try (Socket s = new Socket(ip, p);
                         OutputStream out = s.getOutputStream()) {
                        out.write(("HEARTBEAT;" + aggregatorId + ";" + myIp + ":" + port + "\n").getBytes());
                    } catch (IOException ignored) {}
                }
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void startGossip() {
        new Thread(() -> {
            while (!shutdown) {
                selectRandomNeighbors();
                for (String addr : currentNeighbors) {
                    String[] parts = addr.split(":");
                    String ip = parts[0];
                    int p = Integer.parseInt(parts[1]);
                    try (Socket s = new Socket(ip, p);
                         OutputStream out = s.getOutputStream()) {
                        String replica = serializeReplicaTable();
                        out.write(("GOSSIP;" + aggregatorId + ";" + myIp + ":" + port + ";" + System.currentTimeMillis() + ";" + replica + "\n").getBytes());
                    } catch (IOException ignored) {}
                }
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    static class NodeInfo {
        String id, ip, status;
        int lastNum;
        long timestamp;
        NodeInfo(String id, String ip, int lastNum, String status) {
            this(id, ip, lastNum, status, System.currentTimeMillis());
        }
        NodeInfo(String id, String ip, int lastNum, String status, long timestamp) {
            this.id = id; this.ip = ip; this.lastNum = lastNum; this.status = status; this.timestamp = timestamp;
        }
    }

    private final Map<String, Map<String, Long>> nodeVectorClocks = new ConcurrentHashMap<>();

    private void updateVectorClock(String nodeId) {
        nodeVectorClocks.putIfAbsent(nodeId, new HashMap<>());
        nodeVectorClocks.get(nodeId).put(aggregatorId.toString(), System.currentTimeMillis());
    }

    private String serializeReplicaTable() {
        StringBuilder sb = new StringBuilder();
        for (NodeInfo node : nodes.values()) {
            sb.append(node.id).append(":")
              .append(node.ip).append(":")
              .append(node.lastNum).append(":")
              .append(node.status).append(":")
              .append(node.timestamp).append(":");
            Map<String, Long> clocks = nodeVectorClocks.getOrDefault(node.id, new HashMap<>());
            for (Map.Entry<String, Long> entry : clocks.entrySet()) {
                sb.append(entry.getKey()).append("-").append(entry.getValue()).append("|");
            }
            sb.append(",");
        }
        return sb.toString();
    }

    private void mergeReplicaTable(String replica) {
        String[] entries = replica.split(",");
        for (String entry : entries) {
            if (entry.isEmpty()) continue;
            String[] parts = entry.split(":");
            if (parts.length < 6) continue;
            String id = parts[0];
            int lastNum = Integer.parseInt(parts[2]);
            String status = parts[3];
            long timestamp = Long.parseLong(parts[4]);
            String clocksStr = parts[5];
            Map<String, Long> remoteClocks = new HashMap<>();
            for (String c : clocksStr.split("\\|")) {
                if (c.isEmpty()) continue;
                String[] kv = c.split("-");
                if (kv.length == 2) remoteClocks.put(kv[0], Long.parseLong(kv[1]));
            }
            Map<String, Long> localClocks = nodeVectorClocks.getOrDefault(id, new HashMap<>());
            boolean takeRemote = false;
            for (String agg : remoteClocks.keySet()) {
                long remoteVal = remoteClocks.get(agg);
                long localVal = localClocks.getOrDefault(agg, 0L);
                if (remoteVal > localVal) takeRemote = true;
                localClocks.put(agg, Math.max(remoteVal, localVal));
            }
            nodeVectorClocks.put(id, localClocks);
            if (takeRemote || !nodes.containsKey(id)) {
                nodes.put(id, new NodeInfo(id, "", lastNum, status, timestamp));
            }
        }
    }

    public static byte[] createTLV(byte type, byte[] value) {
        ByteBuffer buffer = ByteBuffer.allocate(1 + 4 + value.length);
        buffer.put(type);
        buffer.putInt(value.length);
        buffer.put(value);
        return buffer.array();
    }

    private int getNodeForTask(int number) {
        List<String> nodeIds = new ArrayList<>(nodes.keySet());
        if (nodeIds.isEmpty()) return -1;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((number + "").getBytes());
            int idx = (hash[0] & 0xFF) % nodeIds.size();
            return idx;
        } catch (Exception e) {
            return 0;
        }
    }

    private String getConsistentNodeForNumber(int number, List<String> nodeIds) {
        if (nodeIds.isEmpty()) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((number + "").getBytes());
            int minDist = Integer.MAX_VALUE;
            String bestNode = nodeIds.get(0);
            for (String nodeId : nodeIds) {
                byte[] nodeHash = md.digest(nodeId.getBytes());
                int dist = Math.abs((hash[0] & 0xFF) - (nodeHash[0] & 0xFF));
                if (dist < minDist) {
                    minDist = dist;
                    bestNode = nodeId;
                }
            }
            return bestNode;
        } catch (Exception e) {
            return nodeIds.get(0);
        }
    }

    private boolean waitForAck(String opId) {
        int tries = 0;
        while (tries < 3) {
            if (ackedOperations.contains(opId)) return true;
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            tries++;
        }
        return false;
    }

    private boolean waitForQuorum(String opId) {
        int count = 1;
        for (String ip : liveAggregators) {
            count++;
        }
        int quorumSize = (seedAggregatorAddresses.size() + 1) / 2 + 1;
        return count >= quorumSize;
    }

    private void startHeartbeatMonitor() {
        new Thread(() -> {
            while (!shutdown) {
                for (String addr : seedAggregatorAddresses) {
                    String[] parts = addr.split(":");
                    String ip = parts[0];
                    int p = Integer.parseInt(parts[1]);
                    if (ip.equals(getMyIp()) && p == port) continue;
                    int missed = missedHeartbeats.getOrDefault(addr, 0);
                    if (missed >= HEARTBEAT_THRESHOLD) {
                        logger.info("[HEARTBEAT] Агрегатор " + addr + " не отвечает. Перераспределение его узлов.");
                        redistributeNodesFromAggregator(ip);
                        missedHeartbeats.put(addr, 0);
                    }
                }
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void handleHeartbeat(String remoteIp) {
        missedHeartbeats.put(remoteIp, 0);
        liveAggregators.add(remoteIp);

        logger.info("[HEARTBEAT] Получен heartbeat от " + remoteIp);
    }


    private String getMyIp() {
        return myIp;
    }

    private void redistributeNodesFromAggregator(String deadAggIp) {
        List<String> affectedNodeIds = new ArrayList<>();
        for (NodeInfo node : nodes.values()) {
            if (node.ip.equals(deadAggIp)) {
                affectedNodeIds.add(node.id);
            }
        }
        if (affectedNodeIds.isEmpty()) return;
        logger.info("[REDISTRIBUTE] Перераспределение задач узлов: " + affectedNodeIds);
        for (String nodeId : affectedNodeIds) {
            logRemoveNode(nodeId);
            nodes.remove(nodeId);
        }
        rebalanceConsistentHashing();
    }

    private void rebalanceConsistentHashing() {
        logger.info("[REBALANCE] Пересчет хэш-кольца и перераспределение задач между живыми узлами");
        if (!allNumbers.isEmpty() && !nodes.isEmpty()) {
            int[] nums = allNumbers.stream().mapToInt(Integer::intValue).toArray();
            distributeTask(nums);
        }
    }

    private void broadcastStateUpdate(String event) {
        for (String ip : currentNeighbors) {
            try (Socket s = new Socket(ip, port);
                 OutputStream out = s.getOutputStream();
                 BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                String opId = "STATE_UPDATE:" + event + ":" + System.currentTimeMillis();
                out.write(("STATE_UPDATE;" + aggregatorId + ";" + System.currentTimeMillis() + ";" + event + ";" + opId + "\n").getBytes());
                out.flush();
                s.setSoTimeout(3000);
                String response = in.readLine();
                if (!"ACK".equals(response)) {
                    logger.warning("[WARN] Нет ACK на STATE_UPDATE от " + ip);
                }
            } catch (IOException ignored) {}
        }
    }

    private void onNodeStateChanged(String nodeId, NodeInfo node) {
        String event = "NODE_UPDATE;" + nodeId + ";" + node.ip + ";" + node.lastNum + ";" + node.status + ";" + node.timestamp;
        broadcastStateUpdate(event);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(myIp))) {            logger.info("Aggregator started on port " + port);
            while (!shutdown) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleNode(socket)).start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка запуска Aggregator", e);
        }
    }

    private void handleNode(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            String remoteIp = socket.getInetAddress().getHostAddress();
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(";");
                String type = parts[0];
                if ("GOSSIP_REQUEST".equals(type)) {
                    String replica = serializeReplicaTable();
                    out.write("GOSSIP_REPLY;" + replica + "\n");
                    out.flush();
                    continue;
                }
                if ("GET_PEERS".equals(type)) {
                    StringBuilder peers = new StringBuilder();
                    for (NodeInfo node : nodes.values()) {
                        if ("active".equals(node.status)) {
                            peers.append(node.ip).append(":9001,");
                        }
                    }
                    out.write("PEERS;" + peers.toString() + "\n");
                    out.flush();
                    continue;
                }
                if ("HEARTBEAT".equals(type)) {
                    handleHeartbeat(remoteIp);
                    if (parts.length >= 3) {
                        String peerAddr = parts[2];
                        if (!seedAggregatorAddresses.contains(peerAddr) && !(peerAddr.equals(myIp + ":" + port))) {
                            seedAggregatorAddresses.add(peerAddr);
                            logger.info("[SEED] Добавлен новый агрегатор: " + peerAddr);
                        }
                    }
                    continue;
                }
                if (readOnlyMode && ("ALERT".equals(type) || "TASK".equals(type))) {
                    logger.severe("[READONLY] Критическая операция запрещена: " + type);
                    continue;
                }
                if ("PING".equals(type)) {
                    String nodeId = parts[1];
                    int lastNum = Integer.parseInt(parts[2]);
                    nodes.put(nodeId, new NodeInfo(nodeId, socket.getInetAddress().getHostAddress(), lastNum, "active"));
                    updateVectorClock(nodeId);
                    logAddNode(nodeId);
                    out.write("PONG\n");
                    out.flush();
                } else if ("ALERT".equals(type)) {
                    int nonPrime = Integer.parseInt(parts[1]);
                    logger.info("Non-prime found: " + nonPrime);
                    wal.add("ALERT;" + nonPrime);
                    writeWAL("ALERT;" + nonPrime);
                    if (tryAcquireAlertLock() && waitForQuorum("ALERT")) {
                        broadcastShutdown();
                        shutdown = true;
                    } else {
                        logger.warning("[ALERT] Нет блокировки или кворума для shutdown!");
                    }
                    break;
                } else if ("RECOVER".equals(type)) {
                    if (parts.length >= 3) {
                        String nodeId = parts[1];
                        int recoveredNum = Integer.parseInt(parts[2]);
                        logger.info("[RECOVER] Узел " + nodeId + " восстановился, ищем незавершённые задачи");
                        NodeInfo node = nodes.get(nodeId);
                        if (node != null) {
                            List<int[]> ranges = nodeAssignedRanges.getOrDefault(nodeId, new ArrayList<>());
                            Set<Integer> toResend = new TreeSet<>();
                            for (int[] range : ranges) {
                                for (int i = range[0]; i <= range[1]; i++) {
                                    if (allNumbers.contains(i) && i > recoveredNum) {
                                        toResend.add(i);
                                    }
                                }
                            }
                            if (!toResend.isEmpty()) {
                                List<Integer> nums = new ArrayList<>(toResend);
                                Collections.sort(nums);
                                int start = nums.get(0), prev = nums.get(0);
                                for (int i = 1; i < nums.size(); i++) {
                                    if (nums.get(i) != prev + 1) {
                                        sendTaskRange(node, start, prev);
                                        start = nums.get(i);
                                    }
                                    prev = nums.get(i);
                                }
                                sendTaskRange(node, start, prev);
                            } else {
                                logger.info("[RECOVER] Нет незавершённых задач для узла " + nodeId);
                            }
                            logAddNode(nodeId);
                        }
                    }
                } else if ("GOSSIP".equals(type)) {
                    // GOSSIP;aggId;ip:port;timestamp;replicaTable
                    if (parts.length >= 5) {
                        String peerAddr = parts[2];
                        if (!seedAggregatorAddresses.contains(peerAddr) && !(peerAddr.equals(myIp + ":" + port))) {
                            seedAggregatorAddresses.add(peerAddr);
                            logger.info("[SEED] Добавлен новый агрегатор: " + peerAddr);
                        }
                        String replicaTable = parts[4];
                        mergeReplicaTable(replicaTable);
                        logger.info("[GOSSIP] Синхронизировано состояние с " + remoteIp);
                    }
                } else if ("TASK".equals(type)) {
                    out.write("ACK\n");
                    out.flush();
                } else if ("STATE_UPDATE".equals(type)) {
                    // STATE_UPDATE;aggId;timestamp;event;opId
                    if (parts.length >= 5) {
                        String event = parts[3];
                        logger.info("[STATE_UPDATE] Получено событие: " + event);
                        if (event.startsWith("NODE_UPDATE;")) {
                            String[] ev = event.split(";");
                            if (ev.length >= 6) {
                                String nodeId = ev[1];
                                String ip = ev[2];
                                int lastNum = Integer.parseInt(ev[3]);
                                String status = ev[4];
                                long timestamp = Long.parseLong(ev[5]);
                                nodes.put(nodeId, new NodeInfo(nodeId, ip, lastNum, status, timestamp));
                                updateVectorClock(nodeId);
                            }
                        }
                        out.write("ACK\n");
                        out.flush();
                    }
                    continue;
                }
                if ("SHUTDOWN".equals(type)) {
                    //SHUTDOWN;opId
                    if (parts.length >= 2) {
                        shutdown = true;
                        out.write("ACK\n");
                        out.flush();
                    }
                    continue;
                }
                if ("ALERT_LOCK".equals(type)) {
                    if (parts.length >= 2) {
                        alertLock = true;
                        alertLockOwner = parts[1];
                        out.write("ACK\n");
                        out.flush();
                    }
                    continue;
                }
                if ("ALERT_LOCK_REQUEST".equals(type)) {
                    // ALERT_LOCK_REQUEST;requesterId;requestTime;leaseMillis;opId
                    if (parts.length >= 5) {
                        String requesterId = parts[1];
                        long requestTime = Long.parseLong(parts[2]);
                        int leaseMillis = Integer.parseInt(parts[3]);
                        handleAlertLockRequest(requesterId, requestTime, leaseMillis, out);
                    }
                    continue;
                }
                if ("ALERT_LOCK_RELEASE".equals(type)) {
                    releaseAlertLock();
                    out.write("ACK\n");
                    out.flush();
                    continue;
                }
            }
        } catch (IOException e) {
        }
    }

    private volatile boolean alertLock = false;
    private volatile String alertLockOwner = null;
    private volatile long alertLockLeaseUntil = 0;
    private final int alertLockLeaseMillis = 10000;

    private boolean tryAcquireAlertLock() {
        String myId = aggregatorId.toString();
        long now = System.currentTimeMillis();
        if (!alertLock || alertLockLeaseUntil < now) {
            int ackCount = 1;
            int quorumSize = (seedAggregatorAddresses.size() + 1) / 2 + 1;
            String opId = "ALERT_LOCK_REQUEST:" + myId + ":" + now;
            for (String ip : seedAggregatorAddresses) {
                if (ip.equals(getMyIp())) continue;
                try (Socket s = new Socket(ip, port);
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                    out.write("ALERT_LOCK_REQUEST;" + myId + ";" + now + ";" + alertLockLeaseMillis + ";" + opId + "\n");
                    out.flush();
                    s.setSoTimeout(3000);
                    String resp = in.readLine();
                    if ("ACK".equals(resp)) ackCount++;
                } catch (IOException ignored) {}
            }
            if (ackCount >= quorumSize) {
                alertLock = true;
                alertLockOwner = myId;
                alertLockLeaseUntil = now + alertLockLeaseMillis;
                broadcastStateUpdate("ALERT_LOCK;" + alertLockOwner + ";" + alertLockLeaseUntil);
                new Thread(() -> {
                    try { Thread.sleep(alertLockLeaseMillis); } catch (InterruptedException ignored) {}
                    if (alertLockOwner != null && alertLockOwner.equals(myId) && System.currentTimeMillis() >= alertLockLeaseUntil) {
                        releaseAlertLock();
                    }
                }).start();
                return true;
            }
            return false;
        }
        return alertLockOwner != null && alertLockOwner.equals(myId) && alertLockLeaseUntil >= now;
    }

    private void releaseAlertLock() {
        alertLock = false;
        alertLockOwner = null;
        alertLockLeaseUntil = 0;
        for (String ip : seedAggregatorAddresses) {
            if (ip.equals(getMyIp())) continue;
            try (Socket s = new Socket(ip, port);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
                out.write("ALERT_LOCK_RELEASE;" + aggregatorId + "\n");
                out.flush();
            } catch (IOException ignored) {}
        }
    }
    private void handleAlertLockRequest(String requesterId, long requestTime, int leaseMillis, BufferedWriter out) throws IOException {
        long now = System.currentTimeMillis();
        if (!alertLock || alertLockLeaseUntil < now) {
            alertLock = true;
            alertLockOwner = requesterId;
            alertLockLeaseUntil = now + leaseMillis;
            out.write("ACK\n");
            out.flush();
            new Thread(() -> {
                try { Thread.sleep(leaseMillis); } catch (InterruptedException ignored) {}
                if (alertLockOwner != null && alertLockOwner.equals(requesterId) && System.currentTimeMillis() >= alertLockLeaseUntil) {
                    alertLock = false;
                    alertLockOwner = null;
                    alertLockLeaseUntil = 0;
                }
            }).start();
        } else {
            out.write("NACK\n");
            out.flush();
        }
    }

    public void distributeTask(int[] numbers) {
        if (nodes.isEmpty()) return;
        for (int n : numbers) allNumbers.add(n);
        List<String> nodeIds = new ArrayList<>(nodes.keySet());
        Map<String, List<Integer>> nodeToNumbers = new HashMap<>();
        for (String nodeId : nodeIds) nodeToNumbers.put(nodeId, new ArrayList<>());
        for (int num : numbers) {
            String assignedNode = getConsistentNodeForNumber(num, nodeIds);
            nodeToNumbers.get(assignedNode).add(num);
            logAssignTask(num, assignedNode);
        }
        for (String nodeId : nodeIds) {
            List<Integer> nums = nodeToNumbers.get(nodeId);
            if (nums.isEmpty()) continue;
            Collections.sort(nums);
            List<int[]> ranges = new ArrayList<>();
            int start = nums.get(0), prev;
            for (int i = 1; i < nums.size(); i++) {
                prev = nums.get(i - 1);
                if (nums.get(i) != prev + 1) {
                    ranges.add(new int[]{start, prev});
                    start = nums.get(i);
                }
            }
            ranges.add(new int[]{start, nums.get(nums.size() - 1)});
            NodeInfo node = nodes.get(nodeId);
            for (int[] range : ranges) {
                sendTaskRange(node, range[0], range[1]);
            }
        }
    }

    private void sendTaskRange(NodeInfo node, int start, int end) {
        String message = "TASK;" + start + "," + end + "\n";
        int attempts = 0;
        boolean acked = false;
        while (attempts < 3 && !acked) {
            attempts++;
            try (Socket s = new Socket(node.ip, 9001);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                writeWAL("SEND_TASK;" + node.id + ";" + start + ";" + end);
                assignRangeToNode(node.id, start, end);
                out.write(message);
                out.flush();
                s.setSoTimeout(3000);
                String response = in.readLine();
                if ("ACK".equals(response)) {
                    acked = true;
                }
            } catch (IOException e) {
                logger.severe("[ERROR] Ошибка отправки диапазона " + start + "-" + end + " узлу " + node.id + ": " + e.getMessage());
            }
        }
        if (!acked) {
            logger.warning("[WARN] Не получен ACK от узла " + node.id + " за диапазон " + start + "-" + end);
        }
    }

    private void broadcastShutdown() {
        for (NodeInfo node : nodes.values()) {
            try (Socket s = new Socket(node.ip, 9001);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                String opId = "SHUTDOWN:" + node.id + ":" + System.currentTimeMillis();
                out.write("SHUTDOWN;" + opId + "\n");
                out.flush();
                s.setSoTimeout(3000);
                String response = in.readLine();
                if (!"ACK".equals(response)) {
                    logger.warning("[WARN] Нет ACK на SHUTDOWN от " + node.id);
                }
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Usage: java org.example.Aggregator <ip> <port> [<seed1_ip:port> <seed2_ip:port> ...]");
            return;
        }
        String myIp = args[0];
        int port = Integer.parseInt(args[1]);
        List<String> seeds = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            seeds.add(args[i]);
        }
        if (!seeds.contains(myIp + ":" + port)) {
            seeds.add(myIp + ":" + port);
        }
        Aggregator aggregator = new Aggregator(myIp, port, seeds);
        new Thread(() -> {
            try {
                aggregator.start();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка запуска Aggregator", e);
            }
        }).start();
        Thread.sleep(2000); 
        aggregator.distributeTask(new int[]{6, 8, 7, 13, 5, 9, 4});
    }
}
