package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.enums.OrderStatus;
import org.example.impl.Baker;
import org.example.impl.BasicOrderQueue;
import org.example.impl.LimitedStorage;
import org.example.utils.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BakerTest {
    private Baker baker;
    private BasicOrderQueue orderQueue;
    private LimitedStorage storage;

    @BeforeEach
    void setUp() {
        orderQueue = new BasicOrderQueue();
        storage = new LimitedStorage(10);
        baker = new Baker(1000, orderQueue, storage);
    }

    @Test
    void testBakerProcessOrder() throws InterruptedException {
        Order order = new Order(1);
        orderQueue.addOrder(order);
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        Thread.sleep(1500);
        assertEquals(OrderStatus.ON_STORAGE, order.getStatus());
        bakerThread.interrupt();
    }

    @Test
    void testFewBakersProcessOrder() throws InterruptedException {
        Order order = new Order(1);
        orderQueue.addOrder(order);
        Thread bakerThread1 = new Thread(baker);
        Thread bakerThread2 = new Thread(new Baker(1000, orderQueue, storage));
        bakerThread1.start();
        bakerThread2.start();
        Thread.sleep(1500);
        assertEquals(OrderStatus.ON_STORAGE, order.getStatus());
        bakerThread1.interrupt();
        bakerThread2.interrupt();
    }

    @Test
    void testFewBakersFewOrders() throws InterruptedException {
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        orderQueue.addOrder(order1);
        orderQueue.addOrder(order2);
        Thread bakerThread1 = new Thread(baker);
        Thread bakerThread2 = new Thread(new Baker(1000, orderQueue, storage));
        bakerThread1.start();
        bakerThread2.start();
        Thread.sleep(1500);
        assertEquals(OrderStatus.ON_STORAGE, order1.getStatus());
        assertEquals(OrderStatus.ON_STORAGE, order2.getStatus());
        bakerThread1.interrupt();
        bakerThread2.interrupt();
    }
}
