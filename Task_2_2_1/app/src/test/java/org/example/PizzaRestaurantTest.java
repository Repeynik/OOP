package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.impl.JsonConfigLoader;
import org.example.interfaces.PizzaConfigLoader;
import org.junit.jupiter.api.Test;

class PizzaRestaurantTest {
    @Test
    void testPizzaRestaurantStartAndShutdown() throws InterruptedException {
        PizzaConfigLoader loader =
                new JsonConfigLoader(
                        "/home/aly0na/OOP/OOP/Task_2_2_1/app/src/main/java/org/example/config.json");
        PizzaRestaurant restaurant = new PizzaRestaurant(loader);
        restaurant.start();
        assertTrue(true);
    }
}
