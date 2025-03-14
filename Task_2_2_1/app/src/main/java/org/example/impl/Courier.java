package org.example.impl;

import org.example.enums.OrderStatus;
import org.example.interfaces.Storage;
import org.example.interfaces.Worker;
import org.example.utils.Order;

import java.util.List;

public class Courier implements Worker {
    private final int trunkCapacity;
    private final Storage storage;
    private List<Order> currentOrders;

    public Courier(int trunkCapacity, Storage storage) {
        this.trunkCapacity = trunkCapacity;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                currentOrders =
                        storage.takePizzas(trunkCapacity, Thread.currentThread().threadId());
                Thread.sleep(1000);
                currentOrders.forEach(
                        order ->
                                order.setStatus(
                                        OrderStatus.DELIVERED,
                                        Long.toString(Thread.currentThread().threadId())));
                currentOrders = null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<Order> getCurrentOrders() {
        return currentOrders;
    }
}
