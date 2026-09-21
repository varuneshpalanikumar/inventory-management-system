# Inventory Management System

A simple console-based Java application built to demonstrate a complete DevOps workflow:
- **Core Java**: Business logic using `ArrayList<Product>`.
- **Maven**: Build automation and dependency management.
- **JUnit 5**: Automated unit testing.
- **Git & GitHub**: Source control and branch management.
- **Ansible**: Automated deployment to a remote Linux server.

## Project Structure
```
inventory-management-system/
├── pom.xml
├── .gitignore
├── README.md
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/inventory/management/
│   │           ├── Product.java
│   │           ├── InventoryManager.java
│   │           └── Main.java
│   └── test/
│       └── java/
│           └── com/inventory/management/
│               └── InventoryManagerTest.java
```

## How to Build & Run
- Compile: `mvn compile`
- Run Tests: `mvn test`
- Package JAR: `mvn package`
- Run App: `java -jar target/inventory-management-system-1.0-SNAPSHOT.jar`
