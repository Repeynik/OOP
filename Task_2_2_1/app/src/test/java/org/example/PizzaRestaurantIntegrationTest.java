package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.impl.JsonConfigLoader;
import org.example.interfaces.PizzaConfigLoader;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

class PizzaRestaurantTestIntegrationTest {
    private static final String LOG_FILE = "log.file";

    @Test
    void testOrderLifecycle() throws Exception {

        PizzaConfigLoader loader = new JsonConfigLoader(Paths.get("testConfig.json"));
        PizzaRestaurant restaurant = new PizzaRestaurant(loader);

        restaurant.start();

        Thread.sleep(7000);

        List<String> logLines = Files.readAllLines(Paths.get(LOG_FILE));
        Map<Integer, List<String>> orderStatuses = parseLogs(logLines);

        System.out.println("Order statuses: " + orderStatuses);

        for (int orderId = 1; orderId <= orderStatuses.size(); orderId++) {
            List<String> statuses = orderStatuses.get(orderId);
            assertValidStatusSequence(statuses, orderId);
        }
    }

    private Map<Integer, List<String>> parseLogs(List<String> logs) {
        return logs.stream()
                .filter(line -> line.contains("INFO: [") && line.contains("] "))
                .collect(
                        Collectors.toMap(
                                line -> Integer.parseInt(line.split("\\[")[1].split("\\]")[0]),
                                line -> {
                                    String status = line.split("] ")[1].split(" ")[0];
                                    return new ArrayList<>(Collections.singletonList(status));
                                },
                                (existing, replacement) -> {
                                    existing.addAll(replacement);
                                    return existing;
                                }));
    }

    private void assertValidStatusSequence(List<String> statuses, int orderId) {
        assertTrue(statuses.contains("QUEUED"), "Order " + orderId + " missing QUEUED");
        assertTrue(statuses.contains("IN_PROGRESS"), "Order " + orderId + " missing IN_PROGRESS");
        assertTrue(statuses.contains("ON_STORAGE"), "Order " + orderId + " missing ON_STORAGE");
        assertTrue(statuses.contains("DELIVERING"), "Order " + orderId + " missing DELIVERING");
        assertTrue(statuses.contains("DELIVERED"), "Order " + orderId + " missing DELIVERED");

        assertEquals(
                "DELIVERED",
                statuses.get(statuses.size() - 1),
                "Order " + orderId + " not delivered");
    }
}
