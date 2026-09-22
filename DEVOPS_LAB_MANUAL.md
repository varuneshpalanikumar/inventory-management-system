# DevOps Laboratory Assignment — End-to-End Workflow & Replication Guide

**Project Name:** Inventory Management System  
**Stack:** Core Java (JDK 17), Apache Maven, JUnit 5, Git, GitHub, Ansible, Linux (Ubuntu / WSL)  
**Author:** Varunesh Palanikumar  
**Repository:** `https://github.com/varuneshpalanikumar/inventory-management-system.git`  

---

## 📑 Table of Contents
1. [Overview & Architecture](#1-overview--architecture)
2. [Prerequisites & System Environment](#2-prerequisites--system-environment)
3. [Q1 — Develop the Core Java Application](#3-q1--develop-the-core-java-application)
4. [Q2 — Maven Project Setup & JUnit 5 Testing](#4-q2--maven-project-setup--junit-5-testing)
5. [Q3 — Git Version Control Setup](#5-q3--git-version-control-setup)
6. [Q4 — Git Branching & Merging (Feature Branch)](#6-q4--git-branching--merging-feature-branch)
7. [Q5 — Uploading Repository to GitHub](#7-q5--uploading-repository-to-github)
8. [Q6 — Maven Packaging & Executable JAR](#8-q6--maven-packaging--executable-jar)
9. [Q7 — Ansible Environment & Inventory Setup](#9-q7--ansible-environment--inventory-setup)
10. [Q8 — Ansible Playbook Development (`deploy.yml`)](#10-q8--ansible-playbook-development-deployyml)
11. [Q9 — Remote Deployment & Service Verification](#11-q9--remote-deployment--service-verification)
12. [Quick Replication Cheat Sheet (All Commands)](#12-quick-replication-cheat-sheet-all-commands)
13. [45 Lab Viva Questions & Answers](#13-45-lab-viva-questions--answers)
14. [Final Verification Checklist](#14-final-verification-checklist)

---

## 1. Overview & Architecture

This project demonstrates a complete DevOps CI/CD and deployment lifecycle using an in-memory console-based Java application. The workflow follows:

```text
          Java Source Code
                 │
                 ▼
      Inventory Management System
                 │
                 ▼
              Maven
                 │
          ┌──────┴──────┐
          │             │
       Compile        JUnit (Automated Tests)
          │             │
          └──────┬──────┘
                 │
                 ▼
          Executable JAR
                 │
                 ▼
             Git Local
                 │
                 ▼
         Feature Branch (feature/low-stock-alert)
                 │
                 ▼
            Merge to Main
                 │
                 ▼
         GitHub Remote Repository
                 │
                 ▼
         Ansible (Control Node: WSL / Ubuntu)
                 │ (SSH / inventory.ini / deploy.yml)
                 ▼
         Remote Linux Server (Managed Node: 172.21.198.141)
                 │
                 ▼
   Verified Java Application Running in /opt/inventory-management
```

---

## 2. Prerequisites & System Environment

| Component | Tool / Environment | Version | Purpose |
|---|---|---|---|
| **Language** | Java Development Kit (JDK) | OpenJDK 17.0.x | Core application runtime & compiler |
| **Build Tool** | Apache Maven | 3.9.x | Dependency management & lifecycle build |
| **Testing** | JUnit Jupiter Engine | 5.10.2 | Unit testing framework |
| **VCS** | Git | 2.4x | Local version control & branching |
| **Remote Host** | GitHub | Cloud | Remote repository hosting |
| **Control Node** | Ubuntu (WSL2) | 24.04 LTS / Ansible 2.16.3 | Automation engine executing playbooks |
| **Managed Node** | Ubuntu VM / WSL Instance | 24.04 LTS / OpenJDK 17 | Remote target deploying the JAR |

---

## 3. Q1 — Develop the Core Java Application

### Objective
Create a simple, menu-driven in-memory console inventory application using Core Java without external databases.

### Folder Structure
```text
src/
└── main/
    └── java/
        └── com/
            └── inventory/
                └── management/
                    ├── Product.java
                    ├── InventoryManager.java
                    └── Main.java
```

### Key Classes & Responsibilities
1. **`Product.java`**: Domain model storing product attributes (`id`, `name`, `price`, `quantity`), constructor, getters, setters, and `toString()`.
2. **`InventoryManager.java`**: Business logic using `ArrayList<Product>`:
   - `addProduct(Product product)`
   - `displayProducts()`
   - `findProduct(int id)`
   - `updateQuantity(int id, int quantity)`
   - `removeProduct(int id)`
3. **`Main.java`**: Interactive console menu supporting operations 1–7, plus a non-interactive `--daemon` mode for remote automated execution.

### Replication Commands
```bash
# Verify Java & Compiler
java -version
javac -version

# Compile manually
javac src/main/java/com/inventory/management/*.java -d bin/

# Run interactive console
java -cp bin com.inventory.management.Main
```

---

## 4. Q2 — Maven Project Setup & JUnit 5 Testing

### Objective
Convert the project into a standard Maven project structure and write automated JUnit 5 tests.

### `pom.xml` Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.inventory</groupId>
    <artifactId>inventory-management-system</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <junit.jupiter.version>5.10.2</junit.jupiter.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.jupiter.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### Unit Tests (`src/test/java/.../InventoryManagerTest.java`)
Tests cover:
- `testAddProduct()`: Asserts product insertion and list growth.
- `testSearchProduct()`: Asserts existing ID returns the correct product.
- `testUpdateQuantity()`: Asserts quantity is modified accurately.
- `testRemoveProduct()`: Asserts item removal and subsequent null search.
- `testSearchNonExistingProduct()`: Asserts ID `999` returns `null`.
- `testLowStockAlert()`: Asserts products below a specified threshold are returned.

### Replication Commands
```bash
# Verify Maven
mvn -version

# Compile application source code
mvn compile

# Execute all automated JUnit 5 tests
mvn test
```

---

## 5. Q3 — Git Version Control Setup

### Objective
Initialize Git version control, configure ignored build artifacts, and establish an initial baseline commit.

### `.gitignore` File
```text
target/
*.class
.idea/
.vscode/
*.iml
.DS_Store
```

### Replication Commands
```bash
# Check Git version
git --version

# Initialize local repository
git init

# Check working tree status
git status

# Stage all files
git add .

# Create baseline commit
git commit -m "Initial inventory management system"

# Verify commit log
git log --oneline
```

---

## 6. Q4 — Git Branching & Merging (Feature Branch)

### Objective
Implement the `low-stock-alert` feature inside an isolated feature branch, test it, and merge it back into `main`.

### Changes Made
1. Added method `getLowStockProducts(int threshold)` to `InventoryManager.java`.
2. Added option `6. Low Stock Alert` to the menu in `Main.java`.
3. Added unit test `testLowStockAlert()` in `InventoryManagerTest.java`.

### Replication Commands
```bash
# Create and switch to new feature branch
git switch -c feature/low-stock-alert

# (Make code edits and verify with mvn test)
mvn test

# Stage and commit feature
git add .
git commit -m "Add low stock alert feature"

# Switch back to main branch
git switch main

# Merge feature branch into main
git merge feature/low-stock-alert

# View branch & commit graph
git log --oneline --graph --all
```

---

## 7. Q5 — Uploading Repository to GitHub

### Objective
Link local Git repository to a remote repository on GitHub and push all branches.

### Replication Commands
```bash
# Rename branch to standard 'main'
git branch -M main

# Link remote origin
git remote add origin https://github.com/varuneshpalanikumar/inventory-management-system.git

# Verify remote configuration
git remote -v

# Push main branch
git push -u origin main

# Push feature branch
git push -u origin feature/low-stock-alert

# Check remote branches
git branch -a
```

---

## 8. Q6 — Maven Packaging & Executable JAR

### Objective
Configure Maven to build a standalone, executable JAR file containing manifest metadata specifying the main class.

### `pom.xml` Plugin Configuration
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.4.1</version>
    <configuration>
        <archive>
            <manifest>
                <mainClass>com.inventory.management.Main</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

### Replication Commands
```bash
# Clean, test, and build packaged JAR
mvn clean package

# Verify created JAR in target directory
ls target/*.jar

# Execute JAR directly
java -jar target/inventory-management-system-1.0-SNAPSHOT.jar
```

---

## 9. Q7 — Ansible Environment & Inventory Setup

### Objective
Configure Ansible on the Control Node (WSL / Ubuntu) to manage the remote server over SSH without password prompts.

### Inventory File (`ansible/inventory.ini`)
```ini
[appserver]
server ansible_host=172.21.198.141 ansible_user=varun ansible_ssh_private_key_file=~/.ssh/id_rsa
```

### Connectivity Verification
```bash
# Check Ansible installation on control node
ansible --version

# Test SSH ping module to remote server
cd ansible/
ansible appserver -i inventory.ini -m ping
```

---

## 10. Q8 — Ansible Playbook Development (`deploy.yml`)

### Objective
Create a declarative Ansible playbook that deploys the application directory, copies the JAR, manages processes, and verifies runtime health.

### Playbook File (`ansible/deploy.yml`)
```yaml
---
- name: Deploy Inventory Management System to Remote Server
  hosts: appserver
  become: true
  vars:
    app_name: inventory-management-system
    app_dir: /opt/inventory-management
    jar_name: inventory-management-system-1.0-SNAPSHOT.jar
    app_user: "{{ ansible_user | default('varun') }}"

  tasks:
    - name: Step 1 - Ensure application directory exists on remote server
      ansible.builtin.file:
        path: "{{ app_dir }}"
        state: directory
        owner: "{{ app_user }}"
        group: "{{ app_user }}"
        mode: '0755'

    - name: Step 2 - Stop any existing running application instance
      ansible.builtin.command:
        cmd: pkill -f "{{ jar_name }}"
      register: stop_process
      failed_when: false
      changed_when: stop_process.rc == 0

    - name: Step 3 - Copy JAR file from control node to remote server
      ansible.builtin.copy:
        src: "../target/{{ jar_name }}"
        dest: "{{ app_dir }}/{{ jar_name }}"
        owner: "{{ app_user }}"
        group: "{{ app_user }}"
        mode: '0755'

    - name: Step 4 - Verify JAR exists on remote server
      ansible.builtin.stat:
        path: "{{ app_dir }}/{{ jar_name }}"
      register: jar_file

    - name: Step 4b - Assert JAR file exists
      ansible.builtin.assert:
        that:
          - jar_file.stat.exists
        fail_msg: "JAR file was not found at {{ app_dir }}/{{ jar_name }}"
        success_msg: "JAR file verified at {{ app_dir }}/{{ jar_name }}"

    - name: Step 5 - Start the Java application in background daemon mode
      ansible.builtin.shell:
        cmd: "nohup java -jar {{ app_dir }}/{{ jar_name }} --daemon > {{ app_dir }}/app.log 2>&1 &"
      become: true
      become_user: "{{ app_user }}"

    - name: Step 5b - Wait briefly for application to initialize
      ansible.builtin.pause:
        seconds: 3

    - name: Step 6 - Verify Java application process is running
      ansible.builtin.command:
        cmd: pgrep -af "{{ jar_name }}"
      register: java_process
      changed_when: false

    - name: Display running Java process details
      ansible.builtin.debug:
        msg: "Java Process Running: {{ java_process.stdout }}"

    - name: Step 6b - Read application log output
      ansible.builtin.command:
        cmd: cat {{ app_dir }}/app.log
      register: app_log
      changed_when: false

    - name: Display Application Startup Log
      ansible.builtin.debug:
        var: app_log.stdout_lines
```

---

## 11. Q9 — Remote Deployment & Service Verification

### Objective
Execute the playbook and verify that the application was deployed, the process is alive, and initial records were seeded.

### Replication Commands
```bash
# Execute deployment playbook
ansible-playbook -i inventory.ini deploy.yml

# SSH into remote server and inspect
ssh -i ~/.ssh/id_rsa varun@172.21.198.141

# Verify deployed directory
ls -la /opt/inventory-management/

# Verify active Java PID
pgrep -af java

# View application output log
cat /opt/inventory-management/app.log
```

---

## 12. Quick Replication Cheat Sheet (All Commands)

```bash
# 1. Clone or initialize
git clone https://github.com/varuneshpalanikumar/inventory-management-system.git
cd inventory-management-system

# 2. Build & run unit tests with Maven
mvn clean compile
mvn test

# 3. Package executable JAR
mvn package

# 4. Test running locally
java -jar target/inventory-management-system-1.0-SNAPSHOT.jar

# 5. Enter ansible directory and test SSH connectivity
cd ansible
ansible appserver -i inventory.ini -m ping

# 6. Deploy to remote server
ansible-playbook -i inventory.ini deploy.yml

# 7. Check remote execution
ssh varun@172.21.198.141 "ls -l /opt/inventory-management && pgrep -af java && cat /opt/inventory-management/app.log"
```

---

## 13. 45 Lab Viva Questions & Answers

1. **What is Java?**  
   *A high-level, object-oriented, platform-independent language executed on the JVM.*
2. **What is an Inventory Management System?**  
   *A software system to track, manage, and monitor items, prices, and stock counts.*
3. **Why did we use ArrayList?**  
   *Provides a dynamically resizable, fast in-memory collection without needing an external database.*
4. **What is Maven?**  
   *A project management and build automation tool based on the Project Object Model (POM).*
5. **Why is Maven used?**  
   *Standardizes project layout, automates dependency downloads, and orchestrates build lifecycles.*
6. **What is pom.xml?**  
   *The primary XML configuration file holding project coordinates, dependencies, and plugins.*
7. **What is groupId?**  
   *The organization/domain package identifier for a project (e.g., `com.inventory`).*
8. **What is artifactId?**  
   *The unique name of the project module or compiled archive (e.g., `inventory-management-system`).*
9. **What is a dependency?**  
   *An external library needed to compile, test, or run code (e.g., JUnit 5).*
10. **What is JUnit?**  
    *An open-source unit testing framework for writing and running automated tests in Java.*
11. **Why do we use JUnit?**  
    *To verify business logic automatically and catch regressions early during the build phase.*
12. **What is `@Test`?**  
    *A JUnit annotation marking a method as a test case to be executed by the test runner.*
13. **What is Git?**  
    *A distributed version control system tracking source code modifications across commits.*
14. **Git vs GitHub?**  
    *Git is the local CLI tool; GitHub is the cloud platform hosting remote Git repositories.*
15. **What is `git init`?**  
    *Initializes a new Git repository by generating a local `.git` metadata directory.*
16. **What is `git add`?**  
    *Stages files from the working directory into the staging area (index).*
17. **What is `git commit`?**  
    *Persists staged snapshot changes to the local repository with an informative log message.*
18. **What is `git status`?**  
    *Displays the state of modified, staged, or untracked files in the working directory.*
19. **What is `git log`?**  
    *Displays the chronological commit history of the current repository branch.*
20. **What is a Git branch?**  
    *A lightweight, movable pointer to a specific commit allowing isolated development.*
21. **Why did we create a feature branch?**  
    *To isolate new development (`low-stock-alert`) without destabilizing the stable `main` branch.*
22. **What is `git merge`?**  
    *Combines commit histories from one branch into another branch.*
23. **What is a merge conflict?**  
    *Occurs when competing changes are made to the same file lines across merging branches.*
24. **What is `git push`?**  
    *Uploads local branch commits to a remote repository like GitHub.*
25. **What is `git clone`?**  
    *Downloads a copy of an entire remote repository to a local machine.*
26. **What is origin?**  
    *The default nickname given by Git to the primary remote repository URL.*
27. **What is Maven lifecycle?**  
    *A sequence of phases: `validate`, `compile`, `test`, `package`, `verify`, `install`, `deploy`.*
28. **What does `mvn clean` do?**  
    *Deletes the `target/` directory and removes all previously compiled artifacts.*
29. **What does `mvn compile` do?**  
    *Compiles Java source files from `src/main/java` into `.class` bytecode in `target/classes`.*
30. **What does `mvn test` do?**  
    *Executes automated unit tests in `src/test/java` using the Surefire plugin.*
31. **What does `mvn package` do?**  
    *Compiles, tests, and bundles code into an artifact such as an executable JAR file.*
32. **What is a JAR file?**  
    *Java Archive: a ZIP-formatted package containing bytecode, resources, and manifest info.*
33. **What is Ansible?**  
    *An open-source, agentless automation engine for configuration management and application deployment.*
34. **What is a control node?**  
    *The machine where Ansible is installed and from which playbooks are run (WSL / Ubuntu).*
35. **What is a managed node?**  
    *The target host or remote server being configured by Ansible (`172.21.198.141`).*
36. **What is an Ansible inventory?**  
    *A file (`inventory.ini`) listing target hosts, groups, IP addresses, and SSH credentials.*
37. **What is an Ansible playbook?**  
    *A YAML file containing plays and tasks to execute on managed nodes.*
38. **What is an Ansible module?**  
    *Reusable scripts executed on remote hosts to manage files, commands, packages, etc.*
39. **What does the Ansible `ping` module do?**  
    *Validates SSH connection and Python environment, returning `"ping": "pong"`.*
40. **How does Ansible connect to the remote server?**  
    *Agentlessly over standard OpenSSH using public/private key authentication.*
41. **What is SSH?**  
    *Secure Shell: a cryptographic network protocol for secure remote access.*
42. **How does Ansible copy the JAR?**  
    *Using the `ansible.builtin.copy` module to transfer files securely over SSH/SFTP.*
43. **How does the playbook start the application?**  
    *Executes `nohup java -jar ... --daemon > app.log 2>&1 &` as a detached daemon process.*
44. **How do we verify the application is running?**  
    *Checks process list using `pgrep -af java` and reads startup logs from `app.log`.*
45. **Explain the complete DevOps workflow.**  
    *Code in Java -> Version control & branch in Git -> Test & package with Maven -> Push to GitHub -> Deploy automatically to Linux server via Ansible -> Verify running application.*

---

## 14. Final Verification Checklist

- [x] **Q1 Core Java Application**: `Product.java`, `InventoryManager.java`, and `Main.java` tested.
- [x] **Q2 Maven Setup & JUnit Testing**: `pom.xml` configured, all 6 unit tests pass.
- [x] **Q3 Git Version Control**: `.gitignore` created, initial commit logged.
- [x] **Q4 Branching & Merging**: `feature/low-stock-alert` implemented and merged cleanly.
- [x] **Q5 Remote GitHub**: Pushed to `varuneshpalanikumar/inventory-management-system`.
- [x] **Q6 Executable JAR**: `target/inventory-management-system-1.0-SNAPSHOT.jar` built and tested.
- [x] **Q7 Ansible Config**: `ansible/inventory.ini` ping test verified with `pong`.
- [x] **Q8 Ansible Playbook**: `ansible/deploy.yml` created and syntax-checked.
- [x] **Q9 Remote Deployment**: Playbook executed with 0 failures; process and logs verified.
