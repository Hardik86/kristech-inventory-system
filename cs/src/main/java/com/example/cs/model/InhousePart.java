package com.example.cs.model;

import jakarta.persistence.Entity;

@Entity
public class InhousePart extends Part {
    private int machineId;

    public InhousePart() {
    }

    public InhousePart(String name, double price, int inv, int minInventory, int maxInventory, int machineId) {
        super(name, price, inv, minInventory, maxInventory);
        this.machineId = machineId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}