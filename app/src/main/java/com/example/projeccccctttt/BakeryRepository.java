package com.example.projeccccctttt;


import java.util.ArrayList;
import java.util.List;

public class BakeryRepository {
    private static final List<BakeryItem> itemList = new ArrayList<>();

    static {
        itemList.add(new BakeryItem("Strawberry Shortcake", "Cake", 1250.0, "High", "24 May 2024"));
        itemList.add(new BakeryItem("Glazed Berry Donut", "Donut", 350.0, "Medium", "23 May 2024"));
        itemList.add(new BakeryItem("Cinnamon Cream Bun", "Bun", 450.0, "Low", "22 May 2024"));
        itemList.add(new BakeryItem("Almond Croissant", "Pastry", 650.0, "High", "21 May 2024"));
    }

    public static List<BakeryItem> getAllItems() {
        return new ArrayList<>(itemList);
    }

    public static void addItem(BakeryItem item) {
        itemList.add(0, item);
    }

    public static void updateItem(BakeryItem updatedItem) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId().equals(updatedItem.getId())) {
                itemList.set(i, updatedItem);
                break;
            }
        }
    }

    public static void deleteItem(String itemId) {
        itemList.removeIf(item -> item.getId().equals(itemId));
    }

    public static BakeryItem getItemById(String itemId) {
        for (BakeryItem item : itemList) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }
}
