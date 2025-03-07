package org.example;

import java.util.List;

public interface Worker extends Runnable {
    List<Order> getCurrentOrders();
}
