package org.example;

import java.util.List;

public class PizzaConfig {
    private int storageCapacity;
    private List<BakerConfig> bakers;
    private List<CourierConfig> couriers;

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
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

class BakerConfig {
    private int speed;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

class CourierConfig {
    private int trunkCapacity;

    public int getTrunkCapacity() {
        return trunkCapacity;
    }

    public void setTrunkCapacity(int trunkCapacity) {
        this.trunkCapacity = trunkCapacity;
    }
}
