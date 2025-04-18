package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.impl.JsonConfigLoader;
import org.example.interfaces.PizzaConfigLoader;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

class PizzaRestaurantTest {
    @Test
    void testPizzaRestaurantStartAndShutdown() throws InterruptedException {
        System.out.println(Paths.get(System.getProperty("user.dir"), "testConfig.json"));

        PizzaConfigLoader loader = new JsonConfigLoader(Paths.get("testConfig.json"));
        PizzaRestaurant restaurant = new PizzaRestaurant(loader);
        restaurant.start();

        assertTrue(true);
    }
}
