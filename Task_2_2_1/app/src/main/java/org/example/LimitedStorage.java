package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LimitedStorage implements Storage {
    private final int capacity;
    private final Queue<Order> orders = new LinkedList<>();

    public LimitedStorage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void putPizza(Order order) throws InterruptedException {
        while (orders.size() >= capacity) {
            wait();
        }
        orders.add(order);
        order.setStatus(OrderStatus.ON_STORAGE);
        notifyAll();
    }

    public synchronized List<Order> takePizzas(int max) throws InterruptedException {
        while (orders.isEmpty()) {
            wait();
        }
        List<Order> taken = new ArrayList<>();
        int count = Math.min(max, orders.size());
        for (int i = 0; i < count; i++) {
            Order order = orders.poll();
            order.setStatus(OrderStatus.DELIVERING);
            taken.add(order);
        }
        notifyAll();
        return taken;
    }

    public synchronized List<Order> getRemainingPizzas() {
        return new ArrayList<>(orders);
    }
}
