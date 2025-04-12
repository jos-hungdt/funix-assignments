package dataType;

public class MyStack<T> {
    private int maxSize;
    private T[] stackArray;
    private int top;

    public MyStack(int maxSize) {
        this.maxSize = maxSize;
        this.stackArray = (T[]) new Object[maxSize];
        this.top = -1;
    }

    /**
     * Insert data to the top of the stack.
     * @param data
     */
    public void push(T data) {
        if (top >= maxSize - 1) {
            System.out.println("[ERROR]: StackOverflow");
            return;
        }

        stackArray[++top] = data;
    }

    /**
     * Get the top element of the Stack and remove it.
     * @return
     */
    public T pop() {
        if (isEmpty()) {
            System.out.println("[ERROR]: Index out of bound");
            return null;
        }

        return stackArray[top--];
    }

    /**
     * Get the top element of the Stack but not delete it.
     * @return
     */
    public T peek() {
        return stackArray[top];
    }

    public void printStack() {
        if (isEmpty()) {
            System.out.println("[INFO]: Stack is empty.");
        }

        T current = pop();
        while (current != null) {
            System.out.println(current.toString());
            
            if (!isEmpty()) {
                current = pop();
            } else {
                current = null;
            }
        }
    }

    /**
     * Check if the Stack is empty.
     * @return
     */
    public boolean isEmpty() {
        return top == -1;
    }
}
