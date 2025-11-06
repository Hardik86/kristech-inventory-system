package com.example.cs.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "part_type")
public abstract class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double price;
    private int inv;
    private int minInventory;
    private int maxInventory;

    // SIMPLE MULTIPACK FIELDS - Added to existing class
    private boolean multipack = false;
    private int packSize = 1;

    @ManyToMany(mappedBy = "parts")
    private Set<Product> products = new HashSet<>();

    // ALL EXISTING CONSTRUCTORS STAY EXACTLY THE SAME
    public Part() {
    }

    public Part(String name, double price, int inv, int minInventory, int maxInventory) {
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.minInventory = minInventory;
        this.maxInventory = maxInventory;
    }

    // ALL EXISTING GETTERS/SETTERS STAY EXACTLY THE SAME
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public int getMinInventory() {
        return minInventory;
    }

    public void setMinInventory(int minInventory) {
        this.minInventory = minInventory;
    }

    public int getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(int maxInventory) {
        this.maxInventory = maxInventory;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    // NEW MULTIPACK GETTERS/SETTERS - Simple addition
    public boolean isMultipack() {
        return multipack;
    }

    public void setMultipack(boolean multipack) {
        this.multipack = multipack;
    }

    public int getPackSize() {
        return packSize;
    }

    public void setPackSize(int packSize) {
        this.packSize = packSize;
        this.multipack = (packSize > 1); // Auto-set multipack flag
    }

    // HELPER METHOD - For display purposes only
    public String getDisplayName() {
        return multipack ? name + " (Pack of " + packSize + ")" : name;
    }

    // HELPER METHOD - For display purposes only
    public double getUnitPrice() {
        return multipack ? price / packSize : price;
    }
}