package com.example.cs.model;

import jakarta.persistence.Entity;

@Entity
public class OutsourcedPart extends Part {
    private String companyName;

    public OutsourcedPart() {}

    public OutsourcedPart(String name, double price, int inv, int minInventory, int maxInventory, String companyName) {
        super(name, price, inv, minInventory, maxInventory);
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}