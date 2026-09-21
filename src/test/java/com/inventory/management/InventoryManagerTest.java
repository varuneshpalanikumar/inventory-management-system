package com.inventory.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryManagerTest {

    private InventoryManager manager;

    @BeforeEach
    void setUp() {
        // Initialize a clean InventoryManager before each test execution
        manager = new InventoryManager();
    }

    @Test
    void testAddProduct() {
        Product product = new Product(101, "Laptop", 55000.0, 10);
        boolean added = manager.addProduct(product);

        assertTrue(added, "Product should be added successfully");
        assertEquals(1, manager.getProducts().size(), "Inventory size should be 1");
        assertEquals("Laptop", manager.findProduct(101).getName(), "Added product name should match");
    }

    @Test
    void testSearchProduct() {
        Product product = new Product(102, "Keyboard", 1500.0, 25);
        manager.addProduct(product);

        Product found = manager.findProduct(102);
        assertNotNull(found, "Product with ID 102 should be found");
        assertEquals(102, found.getId(), "Product ID should match");
        assertEquals("Keyboard", found.getName(), "Product name should match");
    }

    @Test
    void testUpdateQuantity() {
        Product product = new Product(103, "Mouse", 800.0, 30);
        manager.addProduct(product);

        boolean updated = manager.updateQuantity(103, 50);
        assertTrue(updated, "Quantity should update successfully");

        Product found = manager.findProduct(103);
        assertNotNull(found);
        assertEquals(50, found.getQuantity(), "Updated quantity should be 50");
    }

    @Test
    void testRemoveProduct() {
        Product product = new Product(104, "Monitor", 12000.0, 5);
        manager.addProduct(product);

        boolean removed = manager.removeProduct(104);
        assertTrue(removed, "Product should be removed successfully");

        Product found = manager.findProduct(104);
        assertNull(found, "Removed product should no longer be found");
        assertEquals(0, manager.getProducts().size(), "Inventory should be empty");
    }

    @Test
    void testSearchNonExistingProduct() {
        Product found = manager.findProduct(999);
        assertNull(found, "Search for non-existing ID 999 should return null");
    }

    @Test
    void testLowStockAlert() {
        manager.addProduct(new Product(101, "Laptop", 55000.0, 3));
        manager.addProduct(new Product(102, "Keyboard", 1500.0, 25));
        manager.addProduct(new Product(103, "Mouse", 800.0, 2));

        // Threshold = 5 -> Laptop (3) and Mouse (2) should be low stock
        java.util.List<Product> lowStock = manager.getLowStockProducts(5);
        assertEquals(2, lowStock.size(), "Should find 2 low stock products");
        assertTrue(lowStock.stream().anyMatch(p -> p.getName().equals("Laptop")));
        assertTrue(lowStock.stream().anyMatch(p -> p.getName().equals("Mouse")));
        assertFalse(lowStock.stream().anyMatch(p -> p.getName().equals("Keyboard")));
    }
}
