package org.example;

import java.util.*;
import java.util.concurrent.*;

public class PizzaRestaurant {
    private final OrderQueue orderQueue = new BasicOrderQueue();
    private Storage storage;
    private final List<Worker> workers = new ArrayList<>();
    private final List<Thread> workerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private final List<Thread> bakerThreads = new ArrayList<>();
    private Thread orderGeneratorThread;
    private final PizzaConfigLoader configLoader;

    public PizzaRestaurant(PizzaConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public void start() {
        PizzaConfig config = configLoader.loadConfig();
        storage = new LimitedStorage(config.getStorageCapacity());

        config.getBakers()
                .forEach(
                        bakerConfig -> {
                            Worker baker = new Baker(bakerConfig.getSpeed(), orderQueue, storage);
                            registerWorker(baker);
                        });

        config.getCouriers()
                .forEach(
                        courierConfig -> {
                            Worker courier = new Courier(courierConfig.getTrunkCapacity(), storage);
                            registerWorker(courier);
                        });

        startOrderGenerator();
        scheduleShutdown();
    }

    private void registerWorker(Worker worker) {
        Thread thread = new Thread(worker);
        workerThreads.add(thread);
        if (worker instanceof Baker) {
            bakerThreads.add(thread);
        }
        if (worker instanceof Courier) {
            courierThreads.add(thread);
        }
        thread.start();
    }

    private void startOrderGenerator() {
        orderGeneratorThread =
                new Thread(
                        () -> {
                            int orderId = 1;
                            while (!Thread.currentThread().isInterrupted()) {
                                orderQueue.addOrder(new Order(orderId++));
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }
                        });
        orderGeneratorThread.start();
    }

    private void scheduleShutdown() {
        Executors.newScheduledThreadPool(1).schedule(this::shutdown, 1, TimeUnit.HOURS);
    }

    void shutdown() {
        orderGeneratorThread.interrupt();
        bakerThreads.forEach(Thread::interrupt);
        courierThreads.forEach(Thread::interrupt);
        serializePendingOrders();
    }

    private void serializePendingOrders() {
        List<Order> pending = new ArrayList<>(orderQueue.getRemainingOrders());
        pending.addAll(storage.getRemainingPizzas());
        workers.stream()
                .filter(worker -> worker instanceof Baker)
                .map(worker -> (Baker) worker)
                .forEach(
                        baker -> {
                            List<Order> orders = baker.getCurrentOrders();
                            if (orders != null) pending.addAll(orders);
                        });
        workers.stream()
                .filter(worker -> worker instanceof Courier)
                .map(worker -> (Courier) worker)
                .forEach(
                        courier -> {
                            List<Order> orders = courier.getCurrentOrders();
                            if (orders != null) pending.addAll(orders);
                        });
    }
}
