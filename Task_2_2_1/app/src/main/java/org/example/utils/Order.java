package org.example.utils;

import org.example.enums.OrderStatus;

import java.io.Serializable;
import java.util.logging.Logger;

public class Order implements Serializable {
    private final int id;
    private OrderStatus status;

    public Order(int id) {
        this.id = id;
        this.status = OrderStatus.QUEUED;
    }

    public void setStatus(OrderStatus status, String threadId) {
        this.status = status;
        Logger logger = Logger.getLogger(Order.class.getName());
        logger.info("[" + id + "] " + status + " on thread " + threadId);
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
