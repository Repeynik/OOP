package org.example;

import org.example.impl.JsonConfigLoader;
import org.example.interfaces.PizzaConfigLoader;

import java.nio.file.Paths;

public class PizzaSimulation {
    public static void main(String[] args) throws InterruptedException {
        PizzaConfigLoader loader = new JsonConfigLoader(Paths.get("config.json"));
        PizzaRestaurant restaurant = new PizzaRestaurant(loader);
        restaurant.start();
    }
}
