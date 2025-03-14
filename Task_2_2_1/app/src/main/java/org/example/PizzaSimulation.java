package org.example;

import org.example.impl.JsonConfigLoader;
import org.example.interfaces.PizzaConfigLoader;

public class PizzaSimulation {
    public static void main(String[] args) throws InterruptedException {
        PizzaConfigLoader loader =
                new JsonConfigLoader(
                        "/home/aly0na/OOP/OOP/Task_2_2_1/app/src/main/java/org/example/config.json");
        PizzaRestaurant restaurant = new PizzaRestaurant(loader);

        restaurant.start();
    }
}
