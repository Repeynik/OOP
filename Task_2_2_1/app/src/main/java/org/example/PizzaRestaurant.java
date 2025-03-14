package org.example;

import org.example.impl.Baker;
import org.example.impl.BasicOrderQueue;
import org.example.impl.Courier;
import org.example.impl.LimitedStorage;
import org.example.interfaces.OrderQueue;
import org.example.interfaces.PizzaConfigLoader;
import org.example.interfaces.Storage;
import org.example.interfaces.Worker;
import org.example.utils.Order;
import org.example.utils.PizzaConfig;

import java.util.*;
import java.util.logging.Logger;

public class PizzaRestaurant {
    private static final Logger logger = Logger.getLogger(PizzaRestaurant.class.getName());
    private final OrderQueue orderQueue = new BasicOrderQueue();
    private Storage storage;
    private final List<Worker> workers = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private final List<Thread> bakerThreads = new ArrayList<>();
    private Thread orderGeneratorThread;
    private final PizzaConfigLoader configLoader;

    public PizzaRestaurant(PizzaConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public void start() throws InterruptedException {
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
        Thread.sleep(config.getOpenTime());
        shutdown();
    }

    private void registerWorker(Worker worker) {
        Thread thread = new Thread(worker);
        if (worker instanceof Baker) {
            bakerThreads.add(thread);
        }
        if (worker instanceof Courier) {
            courierThreads.add(thread);
        }
        workers.add(worker);
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

    void shutdown() {
        orderGeneratorThread.interrupt();
        logger.info("Restaurant is closed. Cooking remaining pizzas...");
        List<Order> orders = serializePendingOrders();
        while (!orders.isEmpty() || !storage.getRemainingPizzas().isEmpty()) {
            orders = serializePendingOrders();
        }
        logger.info("All pizzas are cooked. Closing workers...");
        bakerThreads.forEach(Thread::interrupt);
        courierThreads.forEach(Thread::interrupt);
        logger.info("Restaurant closed.");
    }

    private List<Order> serializePendingOrders() {
        List<Order> pending = new ArrayList<>(orderQueue.getRemainingOrders());
        pending.addAll(storage.getRemainingPizzas());
        workers.forEach(
                worker -> {
                    List<Order> orders = worker.getCurrentOrders();
                    if (orders != null) pending.addAll(orders);
                });

        return pending;
    }
}
