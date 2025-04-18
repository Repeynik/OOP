package org.example.interfaces;

import org.example.utils.Order;

import java.util.List;

public interface Worker extends Runnable {
    List<Order> getCurrentOrders();
}
