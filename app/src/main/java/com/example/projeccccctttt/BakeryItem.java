package com.example.projeccccctttt;

import java.io.Serializable;
import java.util.UUID;

public class BakeryItem implements Serializable {
    private String id;
    private String name;
    private String category;
    private double price;
    private String priority;
    private String dateAdded;
    private boolean isPurchased;

    public BakeryItem(String name, String category, double price, String priority, String dateAdded) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.price = price;
        this.priority = priority;
        this.dateAdded = dateAdded;
        this.isPurchased = false;
    }

    public BakeryItem(String id, String name, String category, double price, String priority, String dateAdded, boolean isPurchased) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.priority = priority;
        this.dateAdded = dateAdded;
        this.isPurchased = isPurchased;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    public boolean isPurchased() { return isPurchased; }
    public void setPurchased(boolean purchased) { isPurchased = purchased; }
}
