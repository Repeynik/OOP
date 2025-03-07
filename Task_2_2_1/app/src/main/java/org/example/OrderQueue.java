package org.example;

import java.util.List;

public interface OrderQueue {
    void addOrder(Order order);

    Order takeOrder() throws InterruptedException;

    List<Order> getRemainingOrders();
}
