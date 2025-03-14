package org.example.interfaces;

import org.example.utils.Order;

import java.util.List;

public interface Storage {
    void putPizza(Order order) throws InterruptedException;

    List<Order> takePizzas(int max, Long theadId) throws InterruptedException;

    List<Order> getRemainingPizzas();
}
