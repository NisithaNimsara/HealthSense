package util;

import model.disease.DiseaseRecord;
import model.disease.DiseaseRecordLinkedList;

/* Utility class that provides merge sort for custom DiseaseRecordLinkedList.
 * it sorts, by case count (descending: highest first) and sorting by week number (ascending: earliest first)*/

public class MergeSortUtil {


    //Sort by case count (highest to lowest).
    public static void sortLinkedListByCount(DiseaseRecordLinkedList list) {
        list.head = mergeSortByCount(list.head);
    }

    // Recursive merge sort by case count
    private static DiseaseRecordLinkedList.Node mergeSortByCount(DiseaseRecordLinkedList.Node head) {
        if (head == null || head.next == null) return head; // empty or 1 element
        DiseaseRecordLinkedList.Node mid = getMiddle(head); // find middle
        DiseaseRecordLinkedList.Node right = mid.next;
        mid.next = null; // split list in two halves

        DiseaseRecordLinkedList.Node leftSorted = mergeSortByCount(head);
        DiseaseRecordLinkedList.Node rightSorted = mergeSortByCount(right);

        return mergeByCount(leftSorted, rightSorted); // merge back together
    }

    // Merge two sorted lists by case count (descending)
    private static DiseaseRecordLinkedList.Node mergeByCount(DiseaseRecordLinkedList.Node a, DiseaseRecordLinkedList.Node b) {
        DiseaseRecordLinkedList.Node dummy = new DiseaseRecordLinkedList.Node(new DiseaseRecord("", 0, 0));
        DiseaseRecordLinkedList.Node tail = dummy;

        while (a != null && b != null) {
            if (a.data.caseCount >= b.data.caseCount) {
                // copy node from a
                tail.next = new DiseaseRecordLinkedList.Node(
                        new DiseaseRecord(a.data.diseaseName, a.data.weekNumber, a.data.caseCount));
                a = a.next;
            } else {
                // copy node from b
                tail.next = new DiseaseRecordLinkedList.Node(
                        new DiseaseRecord(b.data.diseaseName, b.data.weekNumber, b.data.caseCount));
                b = b.next;
            }
            tail = tail.next;
        }

        // attach remaining nodes
        while (a != null) {
            tail.next = new DiseaseRecordLinkedList.Node(
                    new DiseaseRecord(a.data.diseaseName, a.data.weekNumber, a.data.caseCount));
            a = a.next;
            tail = tail.next;
        }
        while (b != null) {
            tail.next = new DiseaseRecordLinkedList.Node(
                    new DiseaseRecord(b.data.diseaseName, b.data.weekNumber, b.data.caseCount));
            b = b.next;
            tail = tail.next;
        }

        return dummy.next;
    }

    // by week number (ascending order).
    public static void sortLinkedListByWeek(DiseaseRecordLinkedList list) {
        list.head = mergeSortByWeek(list.head);
    }

    // Recursive merge sort by week number
    private static DiseaseRecordLinkedList.Node mergeSortByWeek(DiseaseRecordLinkedList.Node head) {
        if (head == null || head.next == null) return head; // base case
        DiseaseRecordLinkedList.Node mid = getMiddle(head); // find middle
        DiseaseRecordLinkedList.Node right = mid.next;
        mid.next = null;

        DiseaseRecordLinkedList.Node leftSorted = mergeSortByWeek(head);
        DiseaseRecordLinkedList.Node rightSorted = mergeSortByWeek(right);

        return mergeByWeek(leftSorted, rightSorted);
    }

    // Merge two sorted lists by week number (ascending)
    private static DiseaseRecordLinkedList.Node mergeByWeek(DiseaseRecordLinkedList.Node a, DiseaseRecordLinkedList.Node b) {
        DiseaseRecordLinkedList.Node dummy = new DiseaseRecordLinkedList.Node(new DiseaseRecord("", 0, 0));
        DiseaseRecordLinkedList.Node tail = dummy;

        while (a != null && b != null) {
            if (a.data.weekNumber <= b.data.weekNumber) {
                tail.next = new DiseaseRecordLinkedList.Node(
                        new DiseaseRecord(a.data.diseaseName, a.data.weekNumber, a.data.caseCount));
                a = a.next;
            } else {
                tail.next = new DiseaseRecordLinkedList.Node(
                        new DiseaseRecord(b.data.diseaseName, b.data.weekNumber, b.data.caseCount));
                b = b.next;
            }
            tail = tail.next;
        }

        // attach remaining nodes
        while (a != null) {
            tail.next = new DiseaseRecordLinkedList.Node(
                    new DiseaseRecord(a.data.diseaseName, a.data.weekNumber, a.data.caseCount));
            a = a.next;
            tail = tail.next;
        }
        while (b != null) {
            tail.next = new DiseaseRecordLinkedList.Node(
                    new DiseaseRecord(b.data.diseaseName, b.data.weekNumber, b.data.caseCount));
            b = b.next;
            tail = tail.next;
        }

        return dummy.next;
    }

    //Find the middle node of a linked list using the fast/slow pointer method.
    private static DiseaseRecordLinkedList.Node getMiddle(DiseaseRecordLinkedList.Node head) {
        if (head == null) return head;
        DiseaseRecordLinkedList.Node slow = head, fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
