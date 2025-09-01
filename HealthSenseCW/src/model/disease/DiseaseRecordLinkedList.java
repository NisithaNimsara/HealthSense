package model.disease;

//Singly linked list of DiseaseRecord entries.It Used to store the history of disease case count.

public class DiseaseRecordLinkedList {
    // Node inside the linked list, holds one record and pointer to next
    public static class Node {
        public DiseaseRecord data; // the actual disease record
        public Node next;          // next node in list

        public Node(DiseaseRecord data) {
            this.data = data;
        }
    }

    public Node head; // start of the list

    //Add a new record to the end of the list.
    public void append(DiseaseRecord record) {
        Node node = new Node(record);
        if (head == null) {
            head = node; // list was empty, new node becomes head
            return;
        }
        // walk to the last node
        Node cur = head;
        while (cur.next != null) cur = cur.next;
        cur.next = node; // attach new node at end
    }

    //Find the first node that matches the given disease name
    public Node searchByDisease(String disease) {
        Node cur = head;
        while (cur != null) {
            if (cur.data.diseaseName.equalsIgnoreCase(disease)) return cur;
            cur = cur.next;
        }
        return null; // not found
    }

    //Used for undo: Delete the last node in the list
    public void deleteLastMatching(java.util.function.Predicate<DiseaseRecord> pred) {
        if (head == null) return;

        Node prev = null;
        Node cur = head;
        Node lastMatch = null;
        Node lastMatchPrev = null;

        // Traverse and remember the last matching node and its previous
        while (cur != null) {
            if (pred.test(cur.data)) {
                lastMatch = cur;
                lastMatchPrev = prev;
            }
            prev = cur;
            cur = cur.next;
        }

        if (lastMatch == null) return; // nothing to delete

        // Remove lastMatch from list
        if (lastMatchPrev == null) {
            // matched at head
            head = head.next;
        } else {
            lastMatchPrev.next = lastMatch.next;
        }
    }

    //Return the node with the highest caseCount in the list.
    public Node getMaxCaseNode() {
        Node cur = head;
        if (cur == null) return null;

        Node max = cur; // assume first is max initially
        while (cur != null) {
            if (cur.data.caseCount > max.data.caseCount) max = cur;
            cur = cur.next;
        }
        return max;
    }

    //Make a copy of this linked list.
    public DiseaseRecordLinkedList cloneList() {
        DiseaseRecordLinkedList copy = new DiseaseRecordLinkedList();
        Node cur = head;
        while (cur != null) {
            // create a new record with same values and append
            copy.append(new DiseaseRecord(
                    cur.data.diseaseName,
                    cur.data.weekNumber,
                    cur.data.caseCount));
            cur = cur.next;
        }
        return copy;
    }

    // Print every record in this list
    public void printAll() {
        Node cur = head;
        if (cur == null) {
            System.out.println("(empty)");
            return;
        }
        while (cur != null) {
            System.out.printf("  %s week %d count %d%n",
                    cur.data.diseaseName, cur.data.weekNumber, cur.data.caseCount);
            cur = cur.next;
        }
    }
}
