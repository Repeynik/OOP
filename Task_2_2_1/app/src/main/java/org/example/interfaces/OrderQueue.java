package org.example.interfaces;

import org.example.utils.Order;

import java.util.List;

public interface OrderQueue {
    void addOrder(Order order);

    Order takeOrder(Long threadId) throws InterruptedException;

    List<Order> getRemainingOrders();
}
