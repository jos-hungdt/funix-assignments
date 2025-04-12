import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

import dataType.MyBinarySearchTree;
import dataType.MyGraph;
import dataType.Node;
import models.Person;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        MyBinarySearchTree bst = new MyBinarySearchTree();
        MyGraph graph = new MyGraph();

        boolean isUsingApp = true;

        while (isUsingApp) {
            int selectedMenu = menuHandler(sc);
            
            switch (selectedMenu) {
                case 1: {
                    AddNewPersonToBST(sc, bst);
                    break;
                }
                case 2: {
                    System.out.println("[INFO]: Print BST with PreOrder Traversal.");
                    if (bst.getRoot() == null) {
                        System.out.println("[INFO]: Binary Structure Tree is empty!");
                    } else {
                        bst.preOrderTraversal(bst.getRoot());
                    }
                    break;
                }
                case 3: {
                    System.out.println("[INFO]: Print BST with InOrder Traversal.");
                    if (bst.getRoot() == null) {
                        System.out.println("[INFO]: Binary Structure Tree is empty!");
                    } else {
                        bst.inOrderTraversal(bst.getRoot());
                    }
                    break;
                }
                case 4: {
                    System.out.println("[INFO]: Print BST with PostOrder Traversal.");
                    if (bst.getRoot() == null) {
                        System.out.println("[INFO]: Binary Structure Tree is empty!");
                    } else {
                        bst.postOrderTraversal(bst.getRoot());
                    }
                    break;
                }
                case 5: {
                    System.out.println("[INFO]: Print BST with BreadthFirst Traversal.");
                    if (bst.getRoot() == null) {
                        System.out.println("[INFO]: Binary Structure Tree is empty!");
                    } else {
                        bst.breadthFirstTraversal(bst.getRoot());
                    }
                    break;
                }
                case 6: {
                    SearchPersonInBSTById(sc, bst);
                    break;
                }
                case 7: {
                    DeletePersonInBSTById(sc, bst);
                    break;
                }
                case 8: {
                    System.out.println("[INFO]: Balancing the BST!");
                    bst.balance();
                    break;
                }
                case 9: {
                    initGraph(graph);
                    graph.depthFirstTraversal();
                    System.out.println();
                    break;
                }
                case 10: {
                    initGraph(graph);
                    graph.breadthFirstTraversal();
                    System.out.println();
                    break;
                }
                case 11: {
                    initGraph(graph);
                    graph.displayAdj();
                    graph.dijkstra(6);
                    break;
                }
                case 12: {
                    LoadSampleBSTData(bst);
                    // LoadSampleUnbalanceBSTData(bst);
                    break;
                }
                case 0: {
                    System.out.println(">_ Exiting application...");
                    isUsingApp = false;
                    break;
                }            
                default:
                    break;
            }
        }
        sc.close();
    }

    /**
     * Hanlde displaying app menu and validate selected option;
     * @param sc
     * @return an integer that indicates the selected feature.
     */
    private static int menuHandler(Scanner sc) {
        System.out.println("\n+--------[ CSD201X_02-A_VN - Assignment 3 Menu ]-------------+");
        System.out.println("|   [1]: Insert a new employee to BST tree.                  |");
        System.out.println("|   [2]: PreOrder traversal.                                 |");
        System.out.println("|   [3]: Inorder traversal.                                  |");
        System.out.println("|   [4]: PostOrder traversal.                                |");
        System.out.println("|   [5]: Breadth-First traversal.                            |");
        System.out.println("|   [6]: Search person by ID.                                |");
        System.out.println("|   [7]: Delete person by ID.                                |");
        System.out.println("|   [8]: Balancing Binary Search Tree.                       |");
        System.out.println("|   [9]: DFS_Graph.                                          |");
        System.out.println("|   [10]: BFS_Graph.                                         |");
        System.out.println("|   [11]: Dijkstra.                                          |");
        System.out.println("|   [12]: Load BST sample data.                              |");
        System.out.println("|   [0]: Exit.                                               |");
        System.out.println("+------------------------------------------------------------+");

        int option = 0;
        System.out.print(">>> Enter your option: ");
        option = sc.nextInt();

        while (option < 0 || option > 12) {
            System.out.print("[WARN]: Your option is invalid.\n>>> Please re-enter your option: ");
            option = sc.nextInt();
        }

        return option;
    }

    private static void LoadSampleBSTData(MyBinarySearchTree bst) {
        bst.insert(new Person("52", "P50", new java.util.Date(), "BP50"));
        bst.insert(new Person("33", "P33", new java.util.Date(), "BP33"));
        bst.insert(new Person("65", "p65", new java.util.Date(), "BP65"));
        bst.insert(new Person("25", "P25", new java.util.Date(), "BP25"));
        bst.insert(new Person("39", "P39", new java.util.Date(), "BP39"));
        bst.insert(new Person("12", "P12", new java.util.Date(), "BP12"));
        bst.insert(new Person("27", "P27", new java.util.Date(), "BP27"));
        bst.insert(new Person("34", "P34", new java.util.Date(), "BP34"));
        bst.insert(new Person("48", "P48", new java.util.Date(), "BP48"));
        bst.insert(new Person("60", "P60", new java.util.Date(), "BP60"));
        bst.insert(new Person("78", "P78", new java.util.Date(), "BP78"));
        bst.insert(new Person("72", "P72", new java.util.Date(), "BP72"));
        bst.insert(new Person("90", "P90", new java.util.Date(), "BP90"));
    }

    private static void LoadSampleUnbalanceBSTData(MyBinarySearchTree bst) {
        bst.insert(new Person("a", "P5", new java.util.Date(), "BP50"));
        bst.insert(new Person("b", "P7", new java.util.Date(), "BP33"));
        bst.insert(new Person("c", "p12", new java.util.Date(), "BP65"));
        bst.insert(new Person("d", "P15", new java.util.Date(), "BP25"));
        bst.insert(new Person("e", "P25", new java.util.Date(), "BP39"));
        bst.insert(new Person("f", "P27", new java.util.Date(), "BP12"));
        bst.insert(new Person("g", "P42", new java.util.Date(), "BP27"));
        bst.insert(new Person("h", "P47", new java.util.Date(), "BP34"));
        bst.insert(new Person("i", "P50", new java.util.Date(), "BP48"));
    }

    /**
     * Add a new peron to the Binary Search tree.
     */
    private static void AddNewPersonToBST(Scanner sc, MyBinarySearchTree bst) {
        System.out.println("[INFO]: Add new person to the Binary Search Tree.");

        Person person = new Person();
        System.out.print(">>> Enter person ID: ");
        String personId = sc.next();
        sc.nextLine();

        if (bst.search(personId) == null) {
            person.setId(personId);
        } else {
            while (bst.search(personId) != null) {
                System.out.print("[ERROR]: The person ID is already exist, please try another ID! \n >>> Re-Enter new person ID: ");
                personId = sc.nextLine();
            }
            person.setId(personId);
        }

        System.out.print(">>> Enter name: ");
        person.setName(sc.nextLine());

        System.out.print(">>> Enter birth place: ");
        person.setBirthPlace(sc.nextLine());

        System.out.print(">>> Enter date of birth (yyyy-MM-dd): ");
        Date dob = Date.valueOf(LocalDate.parse(sc.nextLine()));
        person.setDateOfBirth(dob);

        bst.insert(person);
        System.out.println("[INFO]: success inserting new person to the Binary Search Tree!");
    }

    /**
     * Delete person in the BST by ID.
     */
    private static void SearchPersonInBSTById(Scanner sc, MyBinarySearchTree bst) {
        System.out.println("[INFO]: Search person in BST by ID!");
        sc.nextLine();
        System.out.print(">>> Enter ID: ");
        String searchId = sc.nextLine();

        Node result = bst.search(searchId);
        if (result == null) {
            System.out.println(">_ Not found any person with the given ID in the BST!");
        } else {
            System.out.println(result.getData().toString());
        }
    }

    /**
     * Delete a person from the BST by ID.
     */
    private static void DeletePersonInBSTById(Scanner sc, MyBinarySearchTree bst) {
        System.out.println("[INFO]: Delete person in BST by ID!");
        sc.nextLine();
        System.out.print(">>> Enter ID: ");
        String idToDelete = sc.nextLine();

        Node result = bst.search(idToDelete);
        if (result == null) {
            System.out.println("[INFO]: Not found such a person to delete.");
            return;
        }

        System.out.println("[INFO]: Deleting " + result.getData().toString());
        bst.delete(idToDelete);
        System.out.println("[INFO]: The requested person has been deleted successfully!");
    }

    private static void initGraph(MyGraph graph) {
        int[][] citiesMatrix = {
            { 0, 10, 9999, 9999, 9999, 9999, 8 },
            { 9999, 0, 2, 10, 25, 80, 9999 },
            { 30, 9999, 0, 8, 3, 20, 9999 },
            { 20, 9999, 9999, 0, 9999, 5, 10 },
            { 9999, 9999, 9999, 4, 0, 9999, 9999 },
            { 8, 9999, 9999, 9999, 9999, 0, 5 },
            { 8, 9999, 9999, 9999, 9999, 9999, 0 }
        };

        graph.setData(citiesMatrix);
    }
}
