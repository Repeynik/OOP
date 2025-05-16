package org.example;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComputeNode {
    private final String id = UUID.randomUUID().toString();
    private final String aggregatorAddress; // ip:port
    private final String aggregatorIp;
    private final int aggregatorPort;
    private int lastNum = -1;
    private volatile boolean running = true;

    private final int udpPort = 9002;
    private final List<String> peerNodeIps = new CopyOnWriteArrayList<>();
    private String myIp = null;
    private final Map<String, Long> lastPong = new ConcurrentHashMap<>();

    private int progressCounter = 0;
    private long lastProgressSave = System.currentTimeMillis();

    private final Map<String, Long> failedNodes = new ConcurrentHashMap<>(); 
    private final Map<String, Long> blockedNodes = new ConcurrentHashMap<>(); 

    public ComputeNode(String aggregatorAddress) {
        this.aggregatorAddress = aggregatorAddress;
        String[] parts = aggregatorAddress.split(":");
        this.aggregatorIp = parts[0];
        this.aggregatorPort = (parts.length > 1) ? Integer.parseInt(parts[1]) : 9000;
    }

    private void handleAggregator(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line = in.readLine();
            if (line == null) return;
            if (line.startsWith("TASK;")) {
                String[] parts = line.substring(5).split(",");
                if (parts.length == 2) {
                    int start = Integer.parseInt(parts[0]);
                    int end = Integer.parseInt(parts[1]);
                    for (int num = start; num <= end; num++) {
                        if (!isPrime(num)) {
                            sendAlert(num);
                            running = false;
                            break;
                        }
                        lastNum = num;
                        progressCounter++;
                        maybeSaveProgress();
                    }
                } else {
                    String[] nums = line.substring(5).split(",");
                    for (String n : nums) {
                        int num = Integer.parseInt(n);
                        if (!isPrime(num)) {
                            sendAlert(num);
                            running = false;
                            break;
                        }
                        lastNum = num;
                        progressCounter++;
                        maybeSaveProgress();
                    }
                }
            } else if (line.startsWith("SHUTDOWN")) {
                running = false;
                sendTasksStopped();
            }
        } catch (IOException e) {
        }
    }

    private void maybeSaveProgress() {
        long now = System.currentTimeMillis();
        if (progressCounter >= 100 || now - lastProgressSave >= 30000) {
            saveProgress();
            progressCounter = 0;
            lastProgressSave = now;
        }
    }

    private void restoreProgress() {
        File f = new File(id + ".progress");
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String s = br.readLine();
                if (s != null) lastNum = Integer.parseInt(s);
            } catch (IOException ignored) {}
        }
    }

    public void start() throws IOException {
        restoreProgress();
        detectMyIp();
        connectAndRecover();
        new Thread(this::pingLoop).start();
        new Thread(this::udpPingLoop).start();
        new Thread(this::udpListenLoop).start();
        new Thread(this::pongMonitorLoop).start();
        new Thread(this::peerDiscoveryLoop).start();
        ServerSocket serverSocket = new ServerSocket(9001);
        while (running) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleAggregator(socket)).start();
        }
        serverSocket.close();
    }

    private void detectMyIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            myIp = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            myIp = "127.0.0.1";
        }
    }

    private void peerDiscoveryLoop() {
        while (running) {
            try (Socket s = new Socket(aggregatorIp, aggregatorPort);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                out.write("GET_PEERS\n");
                out.flush();
                String resp = in.readLine();
                if (resp != null && resp.startsWith("PEERS;")) {
                    String[] peers = resp.substring(6).split(",");
                    peerNodeIps.clear();
                    for (String peer : peers) {
                        if (peer.isEmpty()) continue;
                        String ip = peer.split(":")[0];
                        if (!ip.equals(myIp)) peerNodeIps.add(ip);
                    }
                }
            } catch (Exception ignored) {}
            try { Thread.sleep(10000); } catch (InterruptedException ignored) {}
        }
    }

    private void pingLoop() {
        while (running) {
            try {
                sendPing();
                Thread.sleep(2000);
            } catch (Exception ignored) {}
        }
    }

    private void sendPing() throws IOException {
        try (Socket s = new Socket(aggregatorIp, aggregatorPort);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            out.write("PING;" + id + ";" + lastNum + "\n");
            out.flush();
            in.readLine();
        }
    }

    private void sendAlert(int num) throws IOException {
        try (Socket s = new Socket(aggregatorIp, aggregatorPort);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            out.write("ALERT;" + num + "\n");
            out.flush();
        }
    }

    private void saveProgress() {
        try (FileWriter fw = new FileWriter(id + ".progress")) {
            fw.write(String.valueOf(lastNum));
        } catch (IOException ignored) {}
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++)
            if (n % i == 0) return false;
        return true;
    }

    private void udpPingLoop() {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buf = ("PING;" + id).getBytes();
            while (running) {
                for (String ip : peerNodeIps) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), udpPort);
                    socket.send(packet);
                }
                Thread.sleep(2000);
            }
        } catch (Exception ignored) {}
    }

    private void udpListenLoop() {
        try (DatagramSocket socket = new DatagramSocket(udpPort)) {
            byte[] buf = new byte[64];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                String[] parts = msg.split(";");
                if ("PING".equals(parts[0])) {
                    byte[] pong = ("PONG;" + id).getBytes();
                    DatagramPacket pongPacket = new DatagramPacket(pong, pong.length, packet.getAddress(), packet.getPort());
                    socket.send(pongPacket);
                } else if ("PONG".equals(parts[0])) {
                    String senderId = parts.length > 1 ? parts[1] : packet.getAddress().getHostAddress();
                    lastPong.put(senderId, System.currentTimeMillis());
                }
            }
        } catch (Exception ignored) {}
    }

    private void pongMonitorLoop() {
        while (running) {
            long now = System.currentTimeMillis();
            for (String peer : peerNodeIps) {
                Long last = lastPong.get(peer);
                boolean failed = (last == null || now - last > 6000);
                if (failed) {
                    Long blockUntil = blockedNodes.get(peer);
                    if (blockUntil != null && now < blockUntil) {
                        System.out.println("[PING] Узел " + peer + " заблокирован до " + new java.util.Date(blockUntil));
                        continue;
                    }
                    Long lastFail = failedNodes.get(peer);
                    if (lastFail != null && now - lastFail < 5 * 60 * 1000) {
                        blockedNodes.put(peer, now + 5 * 60 * 1000);
                        System.out.println("[PING] Узел " + peer + " заблокирован на 5 минут!");
                    } else {
                        failedNodes.put(peer, now);
                        System.out.println("[PING] Узел " + peer + " не отвечает! (детектирован сбой)");
                    }
                }
            }
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
        }
    }

    private void sendTasksStopped() {
        try (Socket s = new Socket(aggregatorIp, aggregatorPort);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            out.write("TASKS_STOPPED;" + id + "\n");
            out.flush();
        } catch (IOException e) {
        }
    }

    private void connectAndRecover() {
        List<String> aggregators = new ArrayList<>();
        aggregators.add(aggregatorAddress);
        Set<String> tried = new HashSet<>();
        boolean connected = false;
        for (String addr : aggregators) {
            if (tried.contains(addr)) continue;
            tried.add(addr);
            String[] parts = addr.split(":");
            String ip = parts[0];
            int port = (parts.length > 1) ? Integer.parseInt(parts[1]) : 9000;
            try (Socket s = new Socket(ip, port);
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                 BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                out.write("RECOVER;" + id + ";" + lastNum + "\n");
                out.flush();
                connected = true;
                System.out.println("[RECOVERY] Подключено к агрегатору " + addr);
                break;
            } catch (IOException e) {
            }
        }
        if (!connected) {
            System.err.println("[RECOVERY] Не удалось подключиться ни к одному агрегатору!");
        }
    }

    public static void main(String[] args) throws IOException {
        String aggregatorAddress = args.length > 0 ? args[0] : "127.0.0.1:9000";
        new ComputeNode(aggregatorAddress).start();
    }
}
