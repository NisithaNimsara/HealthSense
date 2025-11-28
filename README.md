# HealthSense – Disease & Healthcare Monitoring System

HealthSense is a Java console-based application designed to manage, analyze, and monitor public-health data using fundamental data structures. It provides tools for tracking diseases, hospitals, medical severity levels, and outbreak reports, while also supporting undo operations and sorted analytics.

This project is ideal for learning data structures and algorithms through a real-world health-monitoring scenario.

---

## 🚀 Features

### 🦠 Disease Management (Linked List)
- Add, view, and remove disease records.
- Stores disease name, symptoms, severity, and case numbers.
- Custom singly linked list implementation.

### 🏥 Hospital Management
- Maintains hospital details and capacity information.
- Includes a hospital manager for creating and displaying hospital records.
- Supports associations with disease records.

### 📊 Severity Level Classification (Binary Search Tree)
- Custom **BST (Binary Search Tree)** for storing severity-based records.
- Efficient searching and categorization of disease severity.
- In-order traversal for automatic sorted output.

### 🚨 Outbreak Report Queue
- FIFO queue system for managing disease outbreak reports.
- Add and process new reports in chronological order.
- Used for simulating real-world outbreak reporting.

### ↩️ Undo System (Stack-based)
- Records user operations (add, delete, update).
- Undo last action using a custom stack implementation.

### 🔍 Merge Sort Utilities
- Provides merge sort algorithms for generating sorted analytics.
- Used for sorting disease data, hospital stats, or severity lists.

### 🖥 CLI System Engine
- All features operate through a clean menu-based command-line interface.
- Central controller manages workflow, user interactions, and data operations.

---

## 📁 Project Structure
```
├─ HealthSenseCW/
│ ├─ src/
│ │ ├─ cli/ # Main CLI and system engine
│ │ ├─ model/
│ │ │ ├─ disease/ # Disease records + linked list
│ │ │ ├─ hospital/ # Hospital + manager classes
│ │ │ ├─ severity/ # Severity BST implementation
│ │ │ ├─ outbreak/ # Outbreak reporting queue
│ │ │ └─ undo/ # Undo manager + operation stack
│ │ └─ util/ # Sorting utilities (MergeSort)
│ ├─ out/ # Compiled build output
│ └─ HealthSenseCW.iml # IntelliJ project file
└─ README.md # (Original README)
```

---

## 🛠 Technologies Used

- **Java 8+**
- Custom Data Structures:
  - Linked List
  - Queue
  - Stack
  - Binary Search Tree
- Sorting Algorithms:
  - Merge Sort
- CLI (Command Line Interface)

---

## ▶️ How to Run

### **Using IntelliJ IDEA**
1. Open project folder: `HealthSenseCW`
2. Ensure the correct SDK is selected (Java 8+)
3. Run `Main.java` located in:
