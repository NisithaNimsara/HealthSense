package model.outbreak;

public class ReportQueue {
    // Internal node for the queue (singly linked)
    static class Node {
        OutbreakReport data; // the report stored here
        Node next;           // next node in the queue

        Node(OutbreakReport data) {
            this.data = data;
        }
    }

    Node front, rear; // front = next to dequeue, rear = last enqueued

    // Add a report to the end of the queue.
    public void enqueue(OutbreakReport report) {
        Node node = new Node(report);
        if (rear == null) {
            // queue is empty, new node is both front and rear
            front = rear = node;
            return;
        }
        // attach to end and move rear pointer
        rear.next = node;
        rear = node;
    }

    // Remove and return the report at the front.
    public OutbreakReport dequeue() {
        if (front == null) return null; // nothing to remove
        OutbreakReport data = front.data;
        front = front.next; // move front forward
        if (front == null) rear = null; // queue became empty, reset rear too
        return data;
    }

    // Look at the front report without removing it.
    public OutbreakReport peek() {
        return front != null ? front.data : null;
    }

    //Print every report in the queue in order.
    public void printAll() {
        Node cur = front;
        if (cur == null) {
            System.out.println("(queue empty)");
            return;
        }
        while (cur != null) {
            System.out.println("  " + cur.data); // uses OutbreakReport.toString()
            cur = cur.next;
        }
    }
}
