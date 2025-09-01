package cli;

import model.hospital.Hospital;
import model.hospital.HospitalManager;
import model.disease.DiseaseRecord;
import model.disease.DiseaseRecordLinkedList;
import model.outbreak.OutbreakReport;
import model.outbreak.OutbreakQueueManager;
import model.severity.SeverityBST;
import model.severity.SeverityRecord;
import model.undo.Operation;
import model.undo.UndoManager;
import util.MergeSortUtil;

import java.util.Scanner;

//Main controller that ties together all parts of the system.

public class SystemEngine {
    // Core subsystem managers
    HospitalManager hospitalManager = new HospitalManager();          // stores and retrieves hospitals
    OutbreakQueueManager queueManager = new OutbreakQueueManager();  // manages outbreak alert queue per region
    SeverityBST severityTree = new SeverityBST();                    // binary search tree for severity ranking
    UndoManager undoManager = new UndoManager();                    // tracks undoable operations
    Scanner scanner = new Scanner(System.in);                       // reads user input

    // Load sample data so the system has something to work with initially
    void seedSampleData() {
        // Add example hospitals with regions
        hospitalManager.addHospital("NHSL", "Western");   // e.g., National Hospital of Sri Lanka - Colombo
        hospitalManager.addHospital("KTH", "Central");    // Kandy Teaching Hospital
        hospitalManager.addHospital("THJ", "Southern");   // Teaching Hospital Jaffna

        // Add sample disease outbreak records (week number and case counts)
        addRecordInternal("NHSL", "Dengue", 10, 120, false);
        addRecordInternal("NHSL", "Dengue", 11, 180, false);
        addRecordInternal("NHSL", "Dengue", 12, 250, false);
        addRecordInternal("NHSL", "COVID-19", 11, 90, false);

        addRecordInternal("KTH", "Leptospirosis", 10, 45, false);
        addRecordInternal("KTH", "Leptospirosis", 11, 60, false);
        addRecordInternal("KTH", "Influenza", 12, 75, false);

        addRecordInternal("THJ", "Cholera", 12, 40, false);
        addRecordInternal("THJ", "Dengue", 11, 95, false);

        // Put some alert reports into the queue for regions
        queueManager.enqueueReport("Western", new OutbreakReport("Western", "NHSL", "Dengue cases rising sharply in Colombo (Week 12)"));
        queueManager.enqueueReport("Central", new OutbreakReport("Central", "KTH", "Leptospirosis trend increasing in Kandy"));
    }


    // Main program
    public void runCLI() {
        while (true) {
            printMenu();  // show options
            String choice = scanner.nextLine().trim();  // get user input
            switch (choice) {
                case "1":
                    handleAddHospital();
                    break;
                case "2":
                    handleAddDiseaseRecord();
                    break;
                case "3":
                    handleSearchByDisease();
                    break;
                case "4":
                    handleSearchHospitalByCount();
                    break;
                case "5":
                    handleSortTrendsByCount();
                    break;
                case "6":
                    handleSortTrendsByWeek();
                    break;
                case "7":
                    handleShowWeeklyTrendForDisease();
                    break;
                case "8":
                    handleDetectSynchronizedPeaks();
                    break;
                case "9":
                    handleEnqueueReport();
                    break;
                case "10":
                    handleViewQueue();
                    break;
                case "11":
                    handleSeverityClassification();
                    break;
                case "12":
                    handleUndo();
                    break;
                case "13":
                    System.out.println("Exiting.");
                    return;  // stop loop and exit
                default:
                    System.out.println("Invalid choice.");
            }
            System.out.println("------"); // separator after each action
        }
    }

    // Print menu to the user
    void printMenu() {
        System.out.println("\n=== HealthSense Outbreak CLI ===");
        System.out.println("1. Add hospital");
        System.out.println("2. Add disease record to hospital");
        System.out.println("3. Search by disease name");
        System.out.println("4. Search hospitals by patient count");
        System.out.println("5. Sort disease records for a hospital by case count (Merge Sort)");
        System.out.println("6. Sort disease records for a hospital by week (chronological)");
        System.out.println("7. Show weekly trend for a disease across hospitals");
        System.out.println("8. Detect synchronized peaks for a disease");
        System.out.println("9. Enqueue outbreak report");
        System.out.println("10. View outbreak queue for region");
        System.out.println("11. View/build severity BST (with traversals)");
        System.out.println("12. Undo last operation");
        System.out.println("13. Exit");
        System.out.print("Select: ");
    }

    // Prompt user and add a new hospital
    void handleAddHospital() {
        System.out.print("Enter hospital name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter region: ");
        String region = scanner.nextLine().trim();
        if (name.isEmpty() || region.isEmpty()) {
            System.out.println("name or region is empty.");
            return;
        }
        hospitalManager.addHospital(name, region);  // add to manager
        System.out.println("Added hospital " + name + " in " + region + ".");
    }

    // Prompt user to add a disease record
    void handleAddDiseaseRecord() {
        try {
            System.out.print("Hospital name: ");
            String h = scanner.nextLine().trim();
            System.out.print("Disease name: ");
            String d = scanner.nextLine().trim();
            System.out.print("Week number (integer): ");
            int w = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Case count: ");
            int c = Integer.parseInt(scanner.nextLine().trim());
            if (h.isEmpty() || d.isEmpty() || w <= 0 || c <= 0) {
                System.out.println("Check your inputs.");
                return;
            }
            addRecordInternal(h, d, w, c, true);  // track undo since this is user's action
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    // Internal helper to add a record to a hospital
    void addRecordInternal(String hospitalName, String disease, int week, int count, boolean trackUndo) {
        Hospital hospital = hospitalManager.getHospitalByName(hospitalName);
        if (hospital == null) {
            System.out.println("Hospital not found: " + hospitalName);
            return;
        }
        DiseaseRecord newRec = new DiseaseRecord(disease, week, count);
        hospital.diseaseHistory.append(newRec);  // add to the linked list of records
        if (trackUndo) {
            // push undo operation: delete this exact record if undone, and re-add if redo
            undoManager.push(new Operation("ADD_RECORD", () -> {
                hospital.diseaseHistory.deleteLastMatching(d -> d.equals(newRec));
            }, () -> {
                hospital.diseaseHistory.append(newRec);
            }));
        }
        System.out.println("Added record to " + hospitalName + ": " + disease + " week " + week + " count " + count);
    }

    // Search all hospitals to find any with a matching disease name
    void handleSearchByDisease() {
        System.out.print("Disease name to search: ");
        String disease = scanner.nextLine().trim();
        boolean found = false;
        for (Hospital h : hospitalManager.getAllHospitals()) {
            DiseaseRecordLinkedList.Node node = h.diseaseHistory.searchByDisease(disease);
            if (node != null) {
                // Print first matching record per hospital
                System.out.printf("Hospital %s has record: %s (week %d, count %d)%n",
                        h.name, node.data.diseaseName, node.data.weekNumber, node.data.caseCount);
                found = true;
            }
        }
        if (!found) System.out.println("No occurrences found.");
    }

    // List hospitals that have disease records meeting a minimum case count and optional disease filter
    void handleSearchHospitalByCount() {
        try {
            System.out.print("Minimum case count to filter: ");
            int threshold = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Disease name (or blank for any): ");
            String disease = scanner.nextLine().trim();
            for (Hospital h : hospitalManager.getAllHospitals()) {
                DiseaseRecordLinkedList.Node current = h.diseaseHistory.head;
                while (current != null) {
                    if (current.data.caseCount >= threshold
                            && (disease.isEmpty() || current.data.diseaseName.equalsIgnoreCase(disease))) {
                        // Print all matching records
                        System.out.printf("Hospital %s has %s cases %d in week %d%n",
                                h.name, current.data.diseaseName, current.data.caseCount, current.data.weekNumber);
                    }
                    current = current.next;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // Sort a given hospital's disease history by case count (using merge sort) and allow undoing that sort
    void handleSortTrendsByCount() {
        System.out.print("Hospital name to sort by case count: ");
        String hname = scanner.nextLine().trim();
        Hospital h = hospitalManager.getHospitalByName(hname);
        if (h == null) {
            System.out.println("Hospital not found.");
            return;
        }
        // make a backup copy for undo
        DiseaseRecordLinkedList backup = h.diseaseHistory.cloneList();
        MergeSortUtil.sortLinkedListByCount(h.diseaseHistory);  // perform sorting
        // push undo/redo for this sort action
        undoManager.push(new Operation("SORT_COUNT", () -> {
            h.diseaseHistory = backup;
        }, () -> {
            MergeSortUtil.sortLinkedListByCount(h.diseaseHistory);
        }));
        System.out.println("Sorted disease records by case count for " + hname + ":");
        h.diseaseHistory.printAll();  // show result
    }

    // Sort by week number (chronological) with undo support
    void handleSortTrendsByWeek() {
        System.out.print("Hospital name to sort by week: ");
        String hname = scanner.nextLine().trim();
        Hospital h = hospitalManager.getHospitalByName(hname);
        if (h == null) {
            System.out.println("Hospital not found.");
            return;
        }
        DiseaseRecordLinkedList backup = h.diseaseHistory.cloneList();  // preserve previous ordering
        MergeSortUtil.sortLinkedListByWeek(h.diseaseHistory);  // sort chronologically
        undoManager.push(new Operation("SORT_WEEK", () -> {
            h.diseaseHistory = backup;
        }, () -> {
            MergeSortUtil.sortLinkedListByWeek(h.diseaseHistory);
        }));
        System.out.println("Sorted disease records chronologically for " + hname + ":");
        h.diseaseHistory.printAll();
    }

    // Show weekly aggregated case counts for a disease, per hospital
    void handleShowWeeklyTrendForDisease() {
        System.out.print("Disease name: ");
        String disease = scanner.nextLine().trim();
        for (Hospital h : hospitalManager.getAllHospitals()) {
            java.util.Map<Integer, Integer> trend = getWeeklyTrend(h, disease);
            if (trend.isEmpty()) continue;  // skip if hospital has none
            System.out.println("Hospital: " + h.name + " (Region: " + h.region + ")");
            java.util.List<Integer> weeks = new java.util.ArrayList<>(trend.keySet());
            java.util.Collections.sort(weeks);  // ensure week order
            for (int wk : weeks) {
                System.out.printf("  Week %d : %d cases%n", wk, trend.get(wk));
            }
        }
    }

    // Detect weeks where multiple hospitals show their peak for the same disease (for synchronized peaks)
    void handleDetectSynchronizedPeaks() {
        try {
            System.out.print("Disease name: ");
            String disease = scanner.nextLine().trim();
            System.out.print("Minimum hospitals overlapping peak (e.g., 1): ");
            int overlap = Integer.parseInt(scanner.nextLine().trim());
            java.util.Map<Integer, Integer> weekFrequency = new java.util.HashMap<>();

            // For each hospital, find its peak week for that disease
            for (Hospital h : hospitalManager.getAllHospitals()) {
                java.util.Map<Integer, Integer> trend = getWeeklyTrend(h, disease);
                if (trend.isEmpty()) continue;
                int peakWeek = -1, max = -1;
                for (var entry : trend.entrySet()) {
                    if (entry.getValue() > max) {  // keep highest
                        max = entry.getValue();
                        peakWeek = entry.getKey();
                    }
                }
                if (peakWeek != -1) {
                    weekFrequency.put(peakWeek, weekFrequency.getOrDefault(peakWeek, 0) + 1);
                }
            }

            // Report weeks where at least `overlap` hospitals had their peak
            System.out.println("Synchronized peak weeks for disease " + disease + ":");
            boolean any = false;
            for (var entry : weekFrequency.entrySet()) {
                if (entry.getValue() >= overlap) {
                    System.out.printf("  Week %d: %d hospitals showing peak%n", entry.getKey(), entry.getValue());
                    any = true;
                }
            }
            if (!any) System.out.println("  (none meeting overlap threshold)");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // Helper to build per-week totals for one hospital and disease
    java.util.Map<Integer, Integer> getWeeklyTrend(Hospital hospital, String disease) {
        java.util.Map<Integer, Integer> trend = new java.util.HashMap<>();
        DiseaseRecordLinkedList.Node cur = hospital.diseaseHistory.head;
        while (cur != null) {
            if (cur.data.diseaseName.equalsIgnoreCase(disease)) {
                // accumulate cases per week
                trend.put(cur.data.weekNumber,
                        trend.getOrDefault(cur.data.weekNumber, 0) + cur.data.caseCount);
            }
            cur = cur.next;
        }
        return trend;
    }

    // Prompt user and add a new outbreak report to the queue
    void handleEnqueueReport() {
        System.out.print("Region: ");
        String region = scanner.nextLine().trim();
        System.out.print("Hospital: ");
        String hospital = scanner.nextLine().trim();
        System.out.print("Summary: ");
        String summary = scanner.nextLine().trim();
        if (region.isEmpty() || hospital.isEmpty()) {
            System.out.println("Region or Hospital is empty.");
            return;
        }
        queueManager.enqueueReport(region, new OutbreakReport(region, hospital, summary));
        System.out.println("Report enqueued.");
    }

    // Prompt user for region and show the queue for that region
    void handleViewQueue() {
        System.out.print("Region to view queue: ");
        String region = scanner.nextLine().trim();
        if (region.isEmpty()) {
            System.out.println("Region is empty.");
            return;
        }
        queueManager.printQueue(region);
    }

    // Build the severity BST from current peaks across hospitals
    void handleSeverityClassification() {
        severityTree.clear(); // reset previous tree
        for (Hospital h : hospitalManager.getAllHospitals()) {
            // get the disease with highest cases for this hospital (its peak)
            DiseaseRecordLinkedList.Node peak = h.diseaseHistory.getMaxCaseNode();
            if (peak != null) {
                severityTree.insert(new SeverityRecord(h.name, peak.data.diseaseName, peak.data.caseCount));
            }
        }
        System.out.println("Severity BST built from current peak counts.");
        System.out.println("Choose traversal: 1-inorder 2-preorder 3-postorder");
        String t = scanner.nextLine().trim();
        switch (t) {
            case "1":
                severityTree.traverseInOrder();
                break;
            case "2":
                severityTree.traversePreOrder();
                break;
            case "3":
                severityTree.traversePostOrder();
                break;
            default:
                System.out.println("Invalid.");
        }
    }

    // Undo the most recent operation if possible
    void handleUndo() {
        if (!undoManager.canUndo()) {
            System.out.println("Nothing to undo.");
            return;
        }
        undoManager.undo();  // perform undo
        System.out.println("Undid last operation.");
    }
}
