package com.inventory.management;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Product> products;

    public InventoryManager() {
        this.products = new ArrayList<>();
    }

    // Add a new product to inventory
    public boolean addProduct(Product product) {
        if (product == null) {
            System.out.println("Cannot add null product.");
            return false;
        }
        if (findProduct(product.getId()) != null) {
            System.out.println("Product with ID " + product.getId() + " already exists.");
            return false;
        }
        products.add(product);
        System.out.println("Product added successfully: " + product.getName());
        return true;
    }

    // Display all products in the inventory
    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty. No products to display.");
            return;
        }
        System.out.println("\n--- Current Inventory ---");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("-------------------------");
    }

    // Find a product by its ID
    public Product findProduct(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    // Update quantity of an existing product
    public boolean updateQuantity(int id, int newQuantity) {
        Product product = findProduct(id);
        if (product != null) {
            product.setQuantity(newQuantity);
            System.out.println("Quantity updated successfully for Product ID: " + id);
            return true;
        }
        System.out.println("Product with ID " + id + " not found.");
        return false;
    }

    // Remove a product by its ID
    public boolean removeProduct(int id) {
        Product product = findProduct(id);
        if (product != null) {
            products.remove(product);
            System.out.println("Product removed successfully: ID " + id);
            return true;
        }
        System.out.println("Product with ID " + id + " not found.");
        return false;
    }

    // Getter for products list (helpful for testing)
    public List<Product> getProducts() {
        return products;
    }
}
