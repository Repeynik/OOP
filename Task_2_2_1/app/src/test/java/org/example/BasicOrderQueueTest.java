package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.enums.OrderStatus;
import org.example.impl.BasicOrderQueue;
import org.example.utils.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BasicOrderQueueTest {
    private BasicOrderQueue orderQueue;

    @BeforeEach
    void setUp() {
        orderQueue = new BasicOrderQueue();
    }

    @Test
    void testAddOrder() {
        Order order = new Order(1);
        orderQueue.addOrder(order);
        assertEquals(OrderStatus.QUEUED, order.getStatus());
    }

    @Test
    void testTakeOrder() throws InterruptedException {
        Order order = new Order(1);
        orderQueue.addOrder(order);
        Order takenOrder = orderQueue.takeOrder(Thread.currentThread().threadId());
        assertEquals(1, takenOrder.getId());
        assertEquals(OrderStatus.IN_PROGRESS, takenOrder.getStatus());
    }

    @Test
    void testGetRemainingOrders() {
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        orderQueue.addOrder(order1);
        orderQueue.addOrder(order2);
        assertEquals(2, orderQueue.getRemainingOrders().size());
    }
}
