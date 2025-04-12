package dataType;

import java.util.Comparator;

public class MyLinkedList<T> {

    private Node<T> head;

    public Node<T> getHead() {
        return this.head;
    }

    public Node<T> getTail() {
        Node<T> node = this.head;
        while (node != null && node.getNextNode() != null) {
            node = node.getNextNode();
        }
        return node;
    }

    private Node<T> getTail(Node<T> head) {
        if (head == null || head.getNextNode() == null) {
            return head;
        }
        Node<T> current = head;
        while (current.getNextNode() != null) {
            current = current.getNextNode();
        }
        return current;
    }

    public void printList() {
        Node<T> node = head;

        while (node != null) {
            System.out.println(node.getData().toString());
            node = node.getNextNode();
        }
    }

    /**
     * Insert a node at the beginning.
     * @param data Node data.
     */
    public void insertAtHead(T data) {
        Node<T> newNode = new Node<T>(data);
        newNode.setNextNode(this.head);
        this.head = newNode;
    }

    public void deleteFromHead() {
        this.head = this.head.getNextNode();
    }

    /**
     * Insert a node at the end.
     * @param data Node data.
     */
    public void insertAtTail(T data) {
        Node<T> newNode = new Node<T>(data);
        if (this.head == null) {
            this.head = newNode;

            return;
        }

        Node<T> linkedList = head;

        // point it to old first node
        while (linkedList.getNextNode() != null) {
            linkedList = linkedList.getNextNode();
        }

        // point first to new first node
        linkedList.setNextNode(newNode);
    }

    public void deleteNode(int position) {
        // Check if the linked list is empty
        if (head == null) {
            return;
        }

        // Case 1: Delete head (position 1)
        if (position == 1) {
            head = head.getNextNode();
            return;
        }

        // Case 2: Delete node at given position
        Node<T> temp = head;
        Node<T> prev = null;

        // Traverse to the position
        for (int i = 1; temp != null && i < position; i++) {
            prev = temp;
            temp = temp.getNextNode();
        }

        // If position is valid, delete the node
        if (temp != null) {
            prev.setNextNode(temp.getNextNode());
        } else {
            System.out.println("[INFO]: Position " + position + " not present in the list.");
        }
    }

    /**
     * Get the number of node in the Linked List.
     */
    public int length() {
        int length = 0;
        Node<T> current = this.head;

        while (current != null) {
            length++;
            current = current.getNextNode();
        }

        return length;
    }

    /**
     * Sort the linked list - QuickSort is used.
     */
    public void sort(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        // Only sort if more than one element
        if (head != null && head.getNextNode() != null) { 
            head = quickSort(head, getTail(), comparator);
        }
    }

    private Node<T> quickSort(Node<T> start, Node<T> end, Comparator<T> comparator) {
        if (start == null || start == end || start.getNextNode() == null) {
            return start;
        }

        // Partition and get the new start and pivot
        Node<T>[] partitioned = partition(start, end, comparator);
        Node<T> pivot = partitioned[1];
        Node<T> newStart = partitioned[0];

        // Find the node before pivot
        Node<T> leftEnd = newStart;
        if (leftEnd != pivot) {
            while (leftEnd != null && leftEnd.getNextNode() != pivot) {
                leftEnd = leftEnd.getNextNode();
            }
            if (leftEnd != null) {
                leftEnd.setNextNode(null);
                // Sort left part
                newStart = quickSort(newStart, leftEnd, comparator);
                // Reconnect
                leftEnd = getTail(newStart);
                if (leftEnd != null) {
                    leftEnd.setNextNode(pivot);
                }
            }
        }

        // Sort right part
        pivot.setNextNode(quickSort(pivot.getNextNode(), end, comparator));

        return newStart;
    }

    private Node<T>[] partition(Node<T> start, Node<T> end, Comparator<T> comparator) {
        Node<T> pivot = start;  // Use first element as pivot
        Node<T> frontier = pivot;
        Node<T> current = pivot.getNextNode();

        // Clear pivot's next to start fresh
        pivot.setNextNode(null);

        while (current != null && current != end.getNextNode()) {
            Node<T> next = current.getNextNode();
            if (comparator.compare(current.getData(), pivot.getData()) < 0) {
                // Put current before pivot
                current.setNextNode(start);
                start = current;
            } else {
                // Put current after pivot
                frontier.setNextNode(current);
                frontier = current;
                frontier.setNextNode(null);
            }
            current = next;
        }

        // Not sure why Java does not allow to create an array for generic type, TBH.
        // That's why Node[] is used instead of Node<T>[] which may cause the warning from IDE.
        return new Node[]{start, pivot};
    }
}
