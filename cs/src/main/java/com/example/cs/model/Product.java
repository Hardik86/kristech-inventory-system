package com.example.cs.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
    private int inv;
    private int minInventory;
    private int maxInventory;

    @ManyToMany
    private Set<Part> parts = new HashSet<>();

    public Product() {}

    public Product(String name, double price, int inv, int minInventory, int maxInventory) {
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.minInventory = minInventory;
        this.maxInventory = maxInventory;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getInv() { return inv; }
    public void setInv(int inv) { this.inv = inv; }
    public int getMinInventory() { return minInventory; }
    public void setMinInventory(int minInventory) { this.minInventory = minInventory; }
    public int getMaxInventory() { return maxInventory; }
    public void setMaxInventory(int maxInventory) { this.maxInventory = maxInventory; }
    public Set<Part> getParts() { return parts; }
    public void setParts(Set<Part> parts) { this.parts = parts; }

    public double getTotalPartsPrice() {
        return parts.stream().mapToDouble(Part::getPrice).sum();
    }

    public boolean isPriceValid() {
        return this.price >= getTotalPartsPrice();
    }

    public boolean isInventoryValid() {
        return inv >= minInventory && inv <= maxInventory;
    }

    public String getInventoryValidationMessage() {
        if (inv < minInventory) return "Inventory " + inv + " is below minimum " + minInventory;
        if (inv > maxInventory) return "Inventory " + inv + " is above maximum " + maxInventory;
        return null;
    }
}