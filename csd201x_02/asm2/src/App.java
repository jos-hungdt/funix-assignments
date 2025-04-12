import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;

import dataType.MyLinkedList;
import dataType.MyQueue;
import dataType.MyStack;
import dataType.Node;
import models.Product;

public class App {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void main(String[] args) throws Exception {
    	// Replace your basePath 
        String basePath = "/home/anonymous/Projects/CSD201x_ASM2/src/assets/";
        String dataFileName = "data.txt";

        MyLinkedList<Product> productList = new MyLinkedList<Product>();;

        Scanner sc = new Scanner(System.in);
        boolean isUsingApp = true;

        while (isUsingApp) {
            int selectedMenu = menuHandler(sc);
            
            switch (selectedMenu) {
                case 1: {
                    System.out.println("[INFO]: Read data file to Linked List and Display to the screen.");
                    productList = LoadDataToLinkedList(basePath + dataFileName);
                    productList.printList();
                    break;
                }
                case 2: {
                    AddProductToLinkedListTail(productList, sc);

                    System.out.println("[INFO]: Linked List after adding new product.");
                    productList.printList();
                    break;
                }
                case 3: {
                    System.out.println("[INFO]: Display all products in the Linked List.");
                    if (productList.length() == 0) {
                        System.out.println("-> No product available in the Linked List.");
                    } else {
                        productList.printList();
                    }
                    break;
                }
                case 4: {
                    WriteLinkedListToFile(productList, basePath + dataFileName);
                    break;
                }
                case 5: {
                    SearchProductById(productList, sc);
                    break;
                }
                case 6: {
                    DeleteProductById(productList, sc);
                    break;
                }
                case 7: {
                    SortLinkedListById(productList);
                    break;
                }
                case 8: {
                    RepresentFirstProductNumberInBinaryUsingRecusive(productList);
                    break;
                }
                case 9: {
                    MyStack<Product> productStack = LoadDataToStack(basePath + dataFileName);
                    productStack.printStack();
                    break;
                }
                case 10:
                    MyQueue<Product> productQueue = LoadDataToQueue(basePath + dataFileName);
                    productQueue.printQueue();
                    break;
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
        System.out.println("\n+-------------------------------------------------------------------+");
        System.out.println("|   [1]: Read data file to Linked List and Display to the screen.   |");
        System.out.println("|   [2]: Add a Product to the end of Linked List.                   |");
        System.out.println("|   [3]: Display all Products in the Linked List.                   |");
        System.out.println("|   [4]: Save all Products in the Linked List to the data file.     |");
        System.out.println("|   [5]: Search product by id.                                      |");
        System.out.println("|   [6]: Delete product by id.                                      |");
        System.out.println("|   [7]: Sort products by id.                                       |");
        System.out.println("|   [8]: Represent the number of products (in base 10 counting sys- |");
        System.out.println("|        tem) of the first element in the Linked List in binary u-  |");
        System.out.println("|        sing recusive method.                                      |");
        System.out.println("|   [9]: Read data file to Stack and Display to the screen.         |");
        System.out.println("|   [10]: Read data file to Queue and Display to the screen.        |");
        System.out.println("|   [0]: Exit.                                                      |");
        System.out.println("+-------------------------------------------------------------------+");

        int option = 0;
        System.out.print(">>> Enter your option: ");
        option = sc.nextInt();

        while (option < 0 || option > 10) {
            System.out.print("[WARN]: Your option is invalid.\n>>> Please re-enter your option: ");
            option = sc.nextInt();
        }

        return option;
    }

    /**
     * Load data from the given file to a Linked List.
     * @param inputFilePath input file path.
     * @return a Linked List with data from the given input file.
     */
    private static MyLinkedList<Product> LoadDataToLinkedList(String inputFilePath) {
        MyLinkedList<Product> productList = new MyLinkedList<Product>();
        
        System.out.println("[INFO]: begin loading data from \"" + inputFilePath + "\".");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                if (values.length != 4) {
                    // Only load valid data row.
                    continue;
                }

                Product product = new Product(
                    Integer.parseInt(values[0]),
                    values[1],
                    Float.parseFloat(values[2]),
                    Integer.parseInt(values[3])
                );
                productList.insertAtHead(product);
            }

            System.out.println("[INFO]: success loading data to Linked List");
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR]: does not exist such file \"" + inputFilePath + "\".");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[ERROR]: an error occured when reading file \"" + inputFilePath + "\".");
            e.printStackTrace();
        }

        return productList;
    }

    /**
     * Insert a new product to the end of the Linked List.
     * @param productList
     */
    private static void AddProductToLinkedListTail(MyLinkedList<Product> productList, Scanner sc) {
        Product product = new Product();

        System.out.print(">>> Enter new product id: ");
        product.setId(sc.nextInt());
        while (SearchProductById(productList, product.getId()) != null) {
            System.out.print("[WARN]: Product ID already exist. \n>>> Re-enter new product id: ");
            product.setId(sc.nextInt());
        }
        sc.nextLine();

        System.out.print(">>> Enter new product name: ");
        product.setName(sc.nextLine());

        System.out.print(">>> Enter new product price: ");
        product.setPrice(sc.nextFloat());

        System.out.print(">>> Enter available number of products: ");
        product.setNumber(sc.nextInt());

        productList.insertAtTail(product);
    }

    /**
     * Write the Linked List data to file
     * @param products
     * @param outputFilePath
     */
    private static void WriteLinkedListToFile(MyLinkedList<Product> products, String outputFilePath) {
        try {
            System.out.println("[INFO]: start writting Linked List to \"" + outputFilePath + "\" file.");
            FileWriter fileWriter = new FileWriter(outputFilePath);

            Node<Product> currentNode = products.getHead();
            while (currentNode != null) {
                fileWriter.append(currentNode.getData().toCsv());
                fileWriter.append(NEW_LINE_SEPARATOR);

                currentNode = currentNode.getNextNode();
            }
            fileWriter.close();
            System.out.println("[INFO]: success writting Linked List to file.");
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR]: does not exist such file \"" + outputFilePath + "\".");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[ERROR]: an error occured when writing file \"" + outputFilePath + "\".");
            e.printStackTrace();
        }
    }

    /**
     * Search product by id
     * @param products
     * @param sc
     */
    private static void SearchProductById(MyLinkedList<Product> products, Scanner sc) {
        Node<Product> currentNode = products.getHead();
        if (currentNode == null) {
            System.out.println("[INFO]: Linked List is empty!");
        }

        System.out.print("[INFO]: search product by id.\n>>> Enter id (number): ");
        int searchId = sc.nextInt();

        Product result = SearchProductById(products, searchId);
        if (result != null) {
            System.out.println("-> Result: " + result.toString());

        } else {
            System.out.println("-1");
            System.out.println("[INFO]: Not found such a product!");
        }
    }

    /**
     * Search a product in Linked List by ID.
     * @param products Linked List of products.
     * @param searchId Product ID to search.
     * @return null if no such a product has the given ID, otherwise returns found Product.
     */
    private static Product SearchProductById(MyLinkedList<Product> products, int searchId) {
        Product result = null;
        Node<Product> currentNode = products.getHead();
        if (currentNode == null) {
            return result;
        }
        
        while (currentNode != null) {
            if (currentNode.getData().getId() == searchId) {
                result = currentNode.getData();
                return result;
            }
            currentNode = currentNode.getNextNode();
        }

        return result;
    }

    /**
     * Delete product by id
     * @param products
     * @param sc
     */
    private static void DeleteProductById(MyLinkedList<Product> products, Scanner sc) {
        Node<Product> currentNode = products.getHead();
        if (currentNode == null) {
            System.out.println("[INFO]: Linked List is empty!");
            return;
        }

        System.out.print("[INFO]: delete product by id.\n>>> Enter id (number) to be deleted: ");
        int searchId = sc.nextInt();
        int position = 1;

        while (currentNode != null) {
            if (currentNode.getData().getId() == searchId) {
                System.out.println("[INFO]: Product to be delete " + currentNode.getData().toString() + "at index = " + position);
                products.deleteNode(position);
                return;
            }
            currentNode = currentNode.getNextNode();
            position++;
        }

        System.out.println("-1");
        System.out.println("[INFO]: Not found such a product!");
    }


    private static void SortLinkedListById(MyLinkedList<Product> productList) {
        productList.sort(Comparator.comparingInt(Product::getId));

        System.out.println("[INFO]: LinkedList After sorting:");
        productList.printList();
    }

    /**
     * Represent First Product Number In Binary Using Recusive
     * @param products
     */
    private static void RepresentFirstProductNumberInBinaryUsingRecusive(MyLinkedList<Product> products) {
        if (products == null || products.length() == 0) {
            System.out.println("[INFO]: The Product Linked List is empty.");
            return;
        }

        Product firstProduct = products.getHead().getData();
        System.out.println(toBinary(firstProduct.getNumber()));
    }

    /**
     * Convert a integer to binary string.
     */
    private static String toBinary(int n) {
        if (n < 2) {
            return Integer.toString(n);
        }

        return toBinary(n / 2) + (n % 2);
    }

    /**
     * Load data from the given file to a Stack.
     * @param inputFilePath input file path.
     * @return a Stack with data from the given input file.
     */
    private static MyStack<Product> LoadDataToStack(String inputFilePath) {
        MyStack<Product> productStack = new MyStack<Product>(9999);
        
        System.out.println("[INFO]: begin loading data from \"" + inputFilePath + "\" to stack.");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                if (values.length != 4) {
                    // Only load valid data row.
                    continue;
                }

                Product product = new Product(
                    Integer.parseInt(values[0]),
                    values[1],
                    Float.parseFloat(values[2]),
                    Integer.parseInt(values[3])
                );
                productStack.push(product);
            }

            System.out.println("[INFO]: success loading data to Stack");
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR]: does not exist such file \"" + inputFilePath + "\".");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[ERROR]: an error occured when reading file \"" + inputFilePath + "\".");
            e.printStackTrace();
        }

        return productStack;
    }

    /**
     * Load data from the given file to a Queue.
     * @param inputFilePath input file path.
     * @return a Queue with data from the given input file.
     */
    private static MyQueue<Product> LoadDataToQueue(String inputFilePath) {
        MyQueue<Product> productQueue = new MyQueue<Product>();
        
        System.out.println("[INFO]: begin loading data from \"" + inputFilePath + "\" to queue.");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                if (values.length != 4) {
                    // Only load valid data row.
                    continue;
                }

                Product product = new Product(
                    Integer.parseInt(values[0]),
                    values[1],
                    Float.parseFloat(values[2]),
                    Integer.parseInt(values[3])
                );
                productQueue.enqueue(product);
            }

            System.out.println("[INFO]: success loading data to Queue");
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR]: does not exist such file \"" + inputFilePath + "\".");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[ERROR]: an error occured when reading file \"" + inputFilePath + "\".");
            e.printStackTrace();
        }

        return productQueue;
    }
}
