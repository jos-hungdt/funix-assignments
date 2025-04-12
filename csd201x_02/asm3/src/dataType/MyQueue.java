package dataType;

public class MyQueue {
    protected Node head, tail;

    public MyQueue() {
        head = tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Node front() {
        if (isEmpty()) {
            System.out.println("[INFO]: queue is empty.");
            return null;
        }
        
        return head;
    }

    public Node dequeue() {
        if (isEmpty()) {
            System.out.println("[INFO]: queue is empty.");
            return null;
        }
        
        Node node = head;
        head = head.getNextNode();

        if (head == null) {
            tail = null;
        }

        return node;
    }

    public void enqueue(Node node) {
        if (isEmpty()) {
            head = tail = node;
            return;
        }

        tail.setNextNode(node);
        tail = tail.getNextNode();
    }

    public void printQueue() {
        if (isEmpty()) {
            System.out.println("[INFO]: queue is empty.");
        }

        Node current = dequeue();
        while (current != null) {
            System.out.println(current.getData().toString());

            if (!isEmpty()) {
                current = dequeue();
            } else {
                current = null;
            }
        }
    }
}
