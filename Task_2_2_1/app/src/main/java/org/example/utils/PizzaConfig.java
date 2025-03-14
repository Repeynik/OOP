package org.example.utils;

import java.util.List;

public class PizzaConfig {
    private int storageCapacity;
    private int openTime;
    private List<BakerConfig> bakers;
    private List<CourierConfig> couriers;

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public List<BakerConfig> getBakers() {
        return bakers;
    }

    public void setBakers(List<BakerConfig> bakers) {
        this.bakers = bakers;
    }

    public List<CourierConfig> getCouriers() {
        return couriers;
    }

    public void setCouriers(List<CourierConfig> couriers) {
        this.couriers = couriers;
    }
}
