package com.inventory.management;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Non-interactive / background service mode for remote server deployment (DevOps demo)
        if (args.length > 0 && args[0].equalsIgnoreCase("--daemon")) {
            System.out.println("==================================================");
            System.out.println("Inventory Management System started successfully.");
            System.out.println("Mode: Non-Interactive / Background Service");
            manager.addProduct(new Product(101, "Laptop", 55000.0, 10));
            manager.addProduct(new Product(102, "Keyboard", 1500.0, 25));
            manager.addProduct(new Product(103, "Mouse", 800.0, 30));
            System.out.println("Total products: " + manager.getProducts().size());
            System.out.println("Application is running.");
            System.out.println("==================================================");
            try {
                // Keep the Java process alive for deployment demonstration
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                System.out.println("Application shutting down.");
            }
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. Display Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Quantity");
            System.out.println("5. Remove Product");
            System.out.println("6. Low Stock Alert");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter Product ID: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Enter Product Name: ");
                        String name = scanner.nextLine().trim();

                        System.out.print("Enter Product Price: ");
                        double price = Double.parseDouble(scanner.nextLine().trim());

                        System.out.print("Enter Product Quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());

                        Product product = new Product(id, name, price, quantity);
                        manager.addProduct(product);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format. ID, price, and quantity must be numeric.");
                    }
                    break;

                case 2:
                    manager.displayProducts();
                    break;

                case 3:
                    try {
                        System.out.print("Enter Product ID to search: ");
                        int searchId = Integer.parseInt(scanner.nextLine().trim());
                        Product found = manager.findProduct(searchId);
                        if (found != null) {
                            System.out.println("Product Found: " + found);
                        } else {
                            System.out.println("Product with ID " + searchId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format! Please enter an integer.");
                    }
                    break;

                case 4:
                    try {
                        System.out.print("Enter Product ID to update quantity: ");
                        int updateId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter New Quantity: ");
                        int newQty = Integer.parseInt(scanner.nextLine().trim());
                        manager.updateQuantity(updateId, newQty);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! ID and quantity must be integers.");
                    }
                    break;

                case 5:
                    try {
                        System.out.print("Enter Product ID to remove: ");
                        int removeId = Integer.parseInt(scanner.nextLine().trim());
                        manager.removeProduct(removeId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format! Please enter an integer.");
                    }
                    break;

                case 6:
                    try {
                        System.out.print("Enter Low Stock Threshold: ");
                        int threshold = Integer.parseInt(scanner.nextLine().trim());
                        java.util.List<Product> lowStock = manager.getLowStockProducts(threshold);
                        if (lowStock.isEmpty()) {
                            System.out.println("No products below threshold " + threshold + ".");
                        } else {
                            System.out.println("\n--- Low Stock Alert (Threshold: " + threshold + ") ---");
                            for (Product p : lowStock) {
                                System.out.println(p.getName() + " - Quantity: " + p.getQuantity());
                            }
                            System.out.println("--------------------------------------------------");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid threshold format! Please enter an integer.");
                    }
                    break;

                case 7:
                    System.out.println("Exiting Inventory Management System. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Please select an option between 1 and 7.");
            }
        }

        scanner.close();
    }
}
