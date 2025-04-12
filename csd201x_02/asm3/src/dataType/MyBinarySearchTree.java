package dataType;

import java.util.ArrayList;

import models.Person;

public class MyBinarySearchTree {
    private Node root;

    public MyBinarySearchTree() {
        this.root = null;
    }

    public MyBinarySearchTree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return this.root;
    }

    /**
     * Removing all nodes from the tree
     */
    public void clear() {
        this.root = null;
    }

    /**
     * Checking if the tree is empty.
     * @return true if the tree is empty.
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Returning all nodes from the tree starting from input node.
     * @param node The node to start counting
     * @return The number of nodes.
     */
    public int count(Node node) {
        return 0;
    }

    /**
     * Insert a new node to the tree.
     * @param data Node data.
     */
    public void insert(Person data) {
        if (this.root == null) {
            this.root = new Node(data);
            return;
        }

        this.root.insert(data);
    }

    /**
     * Traversing the tree using Breadth-First Traversal starting from input node.
     * @param node The node to start traversing.
     */
    public void breadthFirstTraversal(Node node) {
        if (node == null) 
            return;

        MyQueue queue = new MyQueue();
        queue.enqueue(node);

        while (!queue.isEmpty()) { 
            Node current = queue.dequeue();
            
            // Print current node first (visit it)
            System.out.println(current.getData().toString());

            if (current.getLeftNode() != null) {
                queue.enqueue(current.getLeftNode());
            }

            if (current.getRightNode() != null) {
                queue.enqueue(current.getRightNode());
            }
        }
    }
    

    /**
     * Traversing the tree usinIn-Order algorithm starting from input node.
     * @param node The node to start traversing.
     */
    public void preOrderTraversal(Node node) {
        if (node == null) {
            return;
        }

        // Print out the node
        System.out.println(node.getData().toString());

        // first recur on the left subtree
        preOrderTraversal(node.getLeftNode());

        // Recur the right subtree
        preOrderTraversal(node.getRightNode());
    }

    /**
     * Traversing the tree using In-Order algorithm starting from input node.
     * @param node The node to start traversing.
     */
    public void inOrderTraversal(Node node) {
        if (node == null) {
            return;
        }

        // first recur on the left subtree
        inOrderTraversal(node.getLeftNode());

        // Print out the node
        System.out.println(node.getData().toString());

        // Recur the right subtree
        inOrderTraversal(node.getRightNode());
    }

    /**
     * Traversing the tree using Post-Order algorithm starting from input node.
     * @param node The node to start traversing.
     */
    public void postOrderTraversal(Node node) {
        if (node == null) {
            return;
        }

        // first recur on the left subtree
        postOrderTraversal(node.getLeftNode());

        // Recur the right subtree
        postOrderTraversal(node.getRightNode());

        // Print out the node
        System.out.println(node.getData().toString());
    }

    /**
     * Balancing the tree using In-Order. First, traversing the tree using In-Order
     * and adding all nodes to the array. Second, inserting all items from array
     * back to the tree (The second step is not included in this function).
     * @see The balance function below
     * @param list Array of persons added from the tree
     * @param node The root node
     */
    public void inOrder(ArrayList<Person> list, Node node) {
        if (node == null) {
            return;
        }

        // first recur on the left subtree
        inOrder(list, node.getLeftNode());

        // Print out the node
        // System.out.println(node.getData().toString());
        list.add(node.getData());

        // Recur the right subtree
        inOrder(list, node.getRightNode());
    }

    /**
     * Balancing the tree by adding the persons from the array above to the tree by
     * using In-Order.
     * 
     * @param list The array contains all nodes created from method above
     * @param first The first index of the array
     * @param last The last index of the array
     */
    private void balance(ArrayList<Person> list, int first, int last) {
        if (first <= last) {
            int mid = (first + last) / 2;
            insert(list.get(mid));

            balance(list, first, mid - 1);
            balance(list, mid + 1, last);
        }
    }

    /**
     * The main entry point to balance the tree.
     */
    public void balance() {
        // First, calling the inOrder(ArrayList<Person> list, Node node) to
        // copy all items from the tree to array inOrder(ArrayList<Person> list, Node node);
        ArrayList<Person> list = new ArrayList<Person>();
        inOrder(list, this.root);

        // All data is cloned to list, clear the BST for rebalance process.
        clear();

        // Second, calling the balance(ArrayList<Person> list, int first, int last)
        // to balance the tree 
        // balance(ArrayList<Person> list, int first, int last)
        balance(list, 0, list.size() - 1);
    }

    /**
     * Searching a node of the tree by ID, return null if given code does not exists.
     * @return
     */
    public Node search(String id) {
        if (this.root != null) {
            return this.root.find(id);
        }

        return null;
    }

    /**
     * Delete the node has the given ID from the BST.
     * @param id ID of the node to delete.
     */
    public void delete(String id) {
        Node current = this.root;
        Node parent = this.root;
        boolean isLeftChild = false;

        if (current == null) {
            return;
        }

        while (current != null && !current.getData().getId().equals(id)) {
            parent = current;

            if (current.getData().getId().compareTo(id) > 0) {
                current = current.getLeftNode();
                isLeftChild = true;
            } else {
                current = current.getRightNode();
                isLeftChild = false;
            }
        }

        if (current == null) {
            return;
        } 
        
        // This is leaf node cuz it has no child
        // Set its value to null to delete.
        if (current.getLeftNode() == null && current.getRightNode() == null) {
            if (current == this.root) {
                this.root = null;
            } else {
                if (isLeftChild) {
                    parent.setLeftNode(null);
                } else {
                    parent.setRightNode(null);
                }
            }
        } else if (current.getRightNode() == null) {
            // the deletion node has the left child only
            if (current == this.root) {
                this.root = current.getLeftNode();
            } else {
                if (isLeftChild) {
                    parent.setLeftNode(current.getLeftNode());
                } else {
                    parent.setRightNode(current.getLeftNode());
                }
            }
        } else if (current.getLeftNode() == null) {
            // the deletion node has the right child only
            if (current == this.root) {
                this.root = current.getRightNode();
            } else {
                if (isLeftChild) {
                    parent.setLeftNode(current.getRightNode());
                } else {
                    parent.setRightNode(current.getRightNode());
                }
            }
        } else {
            // Delete a Node with Both Children in BST
            // Find the inorder successor (smallest node in right subtree)
            Node successor = getSuccessor(current);
            
            // If current is root
            if (current == this.root) {
                this.root = successor;
            }
            // Link parent to successor
            else if (isLeftChild) {
                parent.setLeftNode(successor);
            } else {
                parent.setRightNode(successor);
            }
            
            // Link successor's left to current's left
            successor.setLeftNode(current.getLeftNode());
        }
    }

    private Node getSuccessor(Node delNode) {
        Node successorParent = delNode;
        Node successor = delNode.getRightNode();
        
        // Go to the leftmost node in right subtree
        while (successor.getLeftNode() != null) {
            successorParent = successor;
            successor = successor.getLeftNode();
        }
        
        // If successor is not just the right child
        if (successor != delNode.getRightNode()) {
            // Connect successorParent's left to successor's right
            successorParent.setLeftNode(successor.getRightNode());
            // Connect successor's right to delNode's right
            successor.setRightNode(delNode.getRightNode());
        }
        
        return successor;
    }
}
