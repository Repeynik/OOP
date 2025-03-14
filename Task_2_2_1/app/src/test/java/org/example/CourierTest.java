package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.enums.OrderStatus;
import org.example.impl.Courier;
import org.example.impl.LimitedStorage;
import org.example.utils.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourierTest {
    private Courier courier;
    private LimitedStorage storage;

    @BeforeEach
    void setUp() {
        storage = new LimitedStorage(10);
        courier = new Courier(3, storage);
    }

    @Test
    void testCourierDeliverOrders() throws InterruptedException {
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        storage.putPizza(order1);
        storage.putPizza(order2);
        Thread courierThread = new Thread(courier);
        courierThread.start();
        Thread.sleep(2000);
        assertEquals(OrderStatus.DELIVERED, order1.getStatus());
        assertEquals(OrderStatus.DELIVERED, order2.getStatus());
        courierThread.interrupt();
    }
}
