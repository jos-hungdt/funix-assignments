package dataType;

import models.Person;

public class Node {
    private Person data;
    private Node left;
    private Node right;
    private Node next;

    public Node (Person data) {
        this.data = data;
    }

    public Person getData() {
        return this.data;
    }

    public void setData(Person data) {
        this.data = data;
    }

    public Node getLeftNode() {
        return this.left;
    }

    public void setLeftNode(Node left) {
        this.left = left;
    }

    public Node getRightNode() {
        return right;
    }

    public void setRightNode(Node right) {
        this.right = right;
    }

    public Node getNextNode() {
        return this.next;
    }

    public void setNextNode(Node nextNode) {
        this.next = nextNode;
    }

    public void insert(Person data) {
        if (data.getId().compareTo(this.data.getId()) > 0) {
            if (this.right == null) {
                this.right = new Node(data);
            } else {
                this.right.insert(data);
            }
        } else {
            if (this.left == null) {
                this.left = new Node(data);
            } else {
                this.left.insert(data);
            }
        }
    }

    public Node find(String id) {
        // if the current node data id equals to the given person id, 
        // return this node.
        if (this.data.getId().equals(id)) {
            return this;
        }

        // if the id is greater than the data id and right child is NOT null,
        // search the right side.
        if (this.data.getId().compareTo(id) < 0 && this.right != null) {
            return this.right.find(id);
        }

        if (this.left != null) {
            return this.left.find(id);
        }

        return null;
    }
}
