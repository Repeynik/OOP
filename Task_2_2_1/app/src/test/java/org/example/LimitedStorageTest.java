package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.impl.LimitedStorage;
import org.example.utils.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class LimitedStorageTest {
    private LimitedStorage storage;

    @BeforeEach
    void setUp() {
        storage = new LimitedStorage(2);
    }

    @Test
    void testAddPizza() throws InterruptedException {
        Order order = new Order(1);
        storage.putPizza(order);
        assertEquals(1, storage.getRemainingPizzas().size());
    }

    @Test
    void testTakePizzas() throws InterruptedException {
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        storage.putPizza(order1);
        storage.putPizza(order2);
        List<Order> takenPizzas = storage.takePizzas(2, Thread.currentThread().threadId());
        assertEquals(2, takenPizzas.size());
        assertEquals(0, storage.getRemainingPizzas().size());
    }

    @Test
    void testStorageCapacity() throws InterruptedException {
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        Order order3 = new Order(3);

        storage.putPizza(order1);
        storage.putPizza(order2);

        Thread thread =
                new Thread(
                        () -> {
                            assertThrows(
                                    IllegalStateException.class, () -> storage.putPizza(order3));
                        });
        thread.start();
        thread.join(1000);

        assertEquals(2, storage.getRemainingPizzas().size());
    }
}
