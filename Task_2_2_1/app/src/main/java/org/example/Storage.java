package org.example;

import java.util.List;

public interface Storage {
    void putPizza(Order order) throws InterruptedException;

    List<Order> takePizzas(int max) throws InterruptedException;

    List<Order> getRemainingPizzas();
}
