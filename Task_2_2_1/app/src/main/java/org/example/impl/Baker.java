package org.example.impl;

import org.example.interfaces.OrderQueue;
import org.example.interfaces.Storage;
import org.example.interfaces.Worker;
import org.example.utils.Order;

import java.util.Collections;
import java.util.List;

public class Baker implements Worker {
    private final int speed;
    private final OrderQueue orderQueue;
    private final Storage storage;
    private Order currentOrder;

    public Baker(int speed, OrderQueue orderQueue, Storage storage) {
        this.speed = speed;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                currentOrder = orderQueue.takeOrder(Thread.currentThread().threadId());
                Thread.sleep(speed);
                storage.putPizza(currentOrder);
                currentOrder = null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public List<Order> getCurrentOrders() {
        return currentOrder != null
                ? Collections.singletonList(currentOrder)
                : Collections.emptyList();
    }
}
