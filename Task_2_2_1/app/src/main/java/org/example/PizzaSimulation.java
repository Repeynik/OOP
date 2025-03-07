package org.example;

public class PizzaSimulation {
    public static void main(String[] args) {
        PizzaConfigLoader loader =
                new JsonConfigLoader(
                        "/home/aly0na/OOP/OOP/Task_2_2_1/app/src/main/java/org/example/config.json");
        PizzaRestaurant restaurant = new PizzaRestaurant(loader);

        restaurant.start();

        try {
            Thread.sleep(60_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        restaurant.shutdown();
    }
}
