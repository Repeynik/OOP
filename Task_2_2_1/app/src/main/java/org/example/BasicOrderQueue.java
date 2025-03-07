package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BasicOrderQueue implements OrderQueue {
    private final Queue<Order> queue = new LinkedList<>();

    public synchronized void addOrder(Order order) {
        queue.add(order);
        order.setStatus(OrderStatus.QUEUED);
        notifyAll();
    }

    public synchronized Order takeOrder() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        Order order = queue.poll();
        order.setStatus(OrderStatus.IN_PROGRESS);
        return order;
    }

    public synchronized List<Order> getRemainingOrders() {
        return new ArrayList<>(queue);
    }
}
