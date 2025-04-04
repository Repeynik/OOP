package org.example.utils;

import org.example.enums.OrderStatus;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Order {
    private final int id;
    private OrderStatus status;
    FileHandler fileHandler;

    {
        try {
            fileHandler = new FileHandler("log.file");
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize FileHandler", e);
        }
    }

    Logger logger = Logger.getLogger(Order.class.getName());

    public Order(int id) {
        this.id = id;
        this.status = OrderStatus.QUEUED;
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
    }

    public void setStatus(OrderStatus status, String threadId) {
        this.status = status;
        logger.info("[" + id + "] " + status + " on thread " + threadId);
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
