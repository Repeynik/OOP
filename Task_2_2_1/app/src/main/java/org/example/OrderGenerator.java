package org.example;

import org.example.interfaces.OrderQueue;
import org.example.utils.Order;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderGenerator implements Runnable {
    private final OrderQueue orderQueue;
    private final AtomicInteger orderId = new AtomicInteger(1);

    public OrderGenerator(OrderQueue orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            orderQueue.addOrder(new Order(orderId.getAndIncrement()));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
