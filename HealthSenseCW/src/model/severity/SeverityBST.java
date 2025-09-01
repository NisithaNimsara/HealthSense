package model.severity;

//Binary search tree ordered by case count (severity)
public class SeverityBST {
    // Node inside the BST, holds one severity record and left/right children
    static class Node {
        SeverityRecord data; // the severity record (hospital name, disease, count)
        Node left, right;    // left = lower severity, right = equal or higher severity

        Node(SeverityRecord data) {
            this.data = data;
        }
    }

    Node root; // root of the tree

    // Insert a new record into the BST by its caseCount.If caseCount is equal or greater, it goes to the right
    public void insert(SeverityRecord rec) {
        root = insertRec(root, rec);
    }

    //Clear the tree
    public void clear() {
        root = null;
    }

    // Recursive helper for insertion
    private Node insertRec(Node node, SeverityRecord rec) {
        if (node == null) return new Node(rec); // place new node here
        if (rec.caseCount < node.data.caseCount) {
            // smaller severity goes left
            node.left = insertRec(node.left, rec);
        } else {
            // equal or larger goes right (keeps duplicates on right)
            node.right = insertRec(node.right, rec);
        }
        return node;
    }

    //In-order traversal prints records from lowest to highest case count.
    public void traverseInOrder() {
        System.out.println("In-order traversal (low to high severity):");
        inOrder(root);
        System.out.println();
    }

    private void inOrder(Node n) {
        if (n == null) return;
        inOrder(n.left);
        System.out.println("  " + n.data);
        inOrder(n.right);
    }

    // Pre-order traversal: root first, then left subtree, then right.
    public void traversePreOrder() {
        System.out.println("Pre-order traversal:");
        preOrder(root);
        System.out.println();
    }

    private void preOrder(Node n) {
        if (n == null) return;
        System.out.println("  " + n.data);
        preOrder(n.left);
        preOrder(n.right);
    }

    //Post-order traversal: left, right, then root.
    public void traversePostOrder() {
        System.out.println("Post-order traversal:");
        postOrder(root);
        System.out.println();
    }

    private void postOrder(Node n) {
        if (n == null) return;
        postOrder(n.left);
        postOrder(n.right);
        System.out.println("  " + n.data);
    }
}
