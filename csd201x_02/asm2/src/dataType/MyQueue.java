package dataType;

public class MyQueue<T> {
    protected Node<T> head, tail;

    public MyQueue() {
        head = tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public T front() {
        if (isEmpty()) {
            System.out.println("[INFO]: queue is empty.");
            return null;
        }
        
        return head.getData();
    }

    public T dequeue() {
        if (isEmpty()) {
            System.out.println("[INFO]: queue is empty.");
            return null;
        }
        
        T data = head.getData();
        head = head.getNextNode();

        if (head == null) {
            tail = null;
        }

        return data;
    }

    public void enqueue(T data) {
        if (isEmpty()) {
            head = tail = new Node<T>(data);
            return;
        }

        tail.setNextNode(new Node<T>(data));
        tail = tail.getNextNode();
    }

    public void printQueue() {
        if (isEmpty()) {
            System.out.println("[INFO]: queue is empty.");
        }

        T current = dequeue();
        while (current != null) {
            System.out.println(current.toString());

            if (!isEmpty()) {
                current = dequeue();
            } else {
                current = null;
            }
        }
    }
}
