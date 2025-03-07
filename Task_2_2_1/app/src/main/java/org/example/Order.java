package org.example;

import java.io.Serializable;

public class Order implements Serializable {
    private final int id;
    private OrderStatus status;

    public Order(int id) {
        this.id = id;
        this.status = OrderStatus.QUEUED;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        System.out.println("[" + id + "] " + status);
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }
}

enum OrderStatus {
    QUEUED,
    IN_PROGRESS,
    ON_STORAGE,
    DELIVERING,
    DELIVERED
}
