import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        // Please update your folder path when running the program
        String folderDir = "/home/herod/Documents/CSDASM/CSD201x_ASM1/assets/";
        String inputFileName = "INPUT.TXT";
        String bubbleSortOutputFileName = "OUTPUT1.TXT";
        String selectionSortOutputFileName = "OUTPUT2.TXT";
        String insertionSortOutputFileName = "OUTPUT3.TXT";
        String linearSearchOutputFileName = "OUTPUT4.TXT";
        String binarySearchOutputFileName = "OUTPUT5.TXT";

        boolean continueUsing = true;
        Scanner sc = new Scanner(System.in);
        float[] originArray = null;
        float[] sortedArray = null;

        while (continueUsing) {
            int option = selectMenu(sc);

            // option 3 to option 7 requires input array exist.
            if (originArray == null 
                && option > 2 
                && option < 7
            ) {
                System.out.println("[ERROR]: The input array does not exist, cannot perform your request.");
                option = -1; // set to -1 will trigger showing the menu cuz default will be reached in the switch statement below.
            } else if (sortedArray == null 
                && option == 7 // option 7 - binary search requires sorted array exist.
            ) {
                System.out.println("[ERROR]: The sorted array does not exist, cannot perform binary search.");
                option = -1; // set to -1 will trigger showing the menu cuz default will be reached in the switch statement below.
            }

            switch (option) {
                case 1:
                    initializeArrayAndSaveToFile(folderDir + inputFileName, sc);                    
                    break;
                case 2:
                    originArray = readInputData(folderDir + inputFileName);
                    break;
                case 3:
                    float[] bubbleSortArray = Arrays.copyOf(originArray, originArray.length);
                    processBubbleSort(bubbleSortArray, folderDir + bubbleSortOutputFileName);
                    sortedArray = bubbleSortArray;
                    break;
                case 4:
                    float[] selectionSortArray = Arrays.copyOf(originArray, originArray.length);
                    processSelectionSort(selectionSortArray, folderDir + selectionSortOutputFileName);
                    sortedArray = selectionSortArray;
                    break;
                case 5:
                    float[] insertionSortArray = Arrays.copyOf(originArray, originArray.length);
                    processInsertionSort(insertionSortArray, folderDir + insertionSortOutputFileName);
                    sortedArray = insertionSortArray;
                    break;
                case 6:
                    processLinearSearch(originArray, sc, folderDir + linearSearchOutputFileName); // linear search will be performed on unsorted array.
                    break;
                case 7:
                    processBinarySearch(sortedArray, sc, folderDir + binarySearchOutputFileName); // all 3 arrays are sorted, just pick the bubble sort array for binany search.
                    break;
                case 8:
                    calculateAlgoExecutionTime(originArray);
                    break;
                case 9:
                    System.out.println("> Exiting application...");
                    continueUsing = false;
                    break;
                default:
                    System.out.println("[INFO]: Select option [1] or [2] to load the input array.");
                    System.out.println("[INFO]: Select option [3], [4] or [5] to sort the input array, before perform option [7].");
                    break;
            }
        }
        sc.close();
    }

    /**
     * Handle displaying app menu and validate selected option.
     * @param sc instance of Scanner - retrieve input from keyboard.
     * @return selected option (1 to 9)
     */
    private static int selectMenu(Scanner sc) {
        System.out.println("\n+------[ CSD201x_02_VN - Assignment 1 MENU ]------+");
        System.out.println("|    [1]: Enter input array.                      |");
        System.out.println("|    [2]: Show input array from INPUT.TXT.        |");
        System.out.println("|    [3]: Bubble sort.                            |");
        System.out.println("|    [4]: Selection sort.                         |");
        System.out.println("|    [5]: Insertion sort.                         |");
        System.out.println("|    [6]: Linear search.                          |");
        System.out.println("|    [7]: Binary search.                          |");
        System.out.println("|    [8]: Analyze algorithm execution time.       |");
        System.out.println("|    [9]: Exit.                                   |");
        System.out.println("+-------------------------------------------------+");

        int option = 0;        
        System.out.print("Enter your option: ");
        option = sc.nextInt();

        while (option < 1 || option > 9) {
            System.out.print("Your option is invalid. Please re-enter your option: ");
            option = sc.nextInt();
        }
        return option;
    }

    //region Main feature
    /**
     * This function satisfies the first requirement: 
     * "1. Nhập một dãy các số thực gồm có n số (n<=20) lưu vào tệp INPUT.TXT. 
     * Chương trình cho phép người dùng nhập vào độ dài của mảng lưu vào biến n. 
     * Cho phép người dùng nhập vào giá trị của từng thành phần trong mảng.Dữ liệu được được lưu vào mảng."
     * @param inputFilePath the input file path which will be used to writting the input array.
     */
    private static void initializeArrayAndSaveToFile(String inputFilePath, Scanner sc) {
        System.out.println("[INFO]: starting enter array.");
        int n = -1;
        
        boolean enterredN = false;
        while(!enterredN) {
            System.out.print("Enter the length of input array, N = ");
            n = sc.nextInt();

            if (n > 0 && n <= 20) {
                enterredN = true;
            } 
            else {
                System.out.println("[WARN]: N must be greater than 0 and lower than 20!");
            }
        }

        System.out.println("[INFO]: starting enter array elements.");
        System.out.println("Please enter " + n + " decimal, each number on single line:");
        float[] arr = new float[n];

        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextFloat();
        }
    
        try {
            File inputFile = new File(inputFilePath);
            if (inputFile.createNewFile()) {
                System.out.println("[INFO]: input file is created!");
            }

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(inputFilePath));
            for (int i = 0; i < n; i++) {
                outputWriter.write(Float.toString(arr[i]));
                outputWriter.newLine();
            }

            outputWriter.flush();
            outputWriter.close();
            System.out.println("[INFO]: success writing array to \"" + inputFilePath + "\"");
        }
        catch (IOException ex) {
            System.out.println("[ERROR]: an error occured: " + ex);
        }
    }

    /**
     * This function satisfies the 2nd requirement: 
     * "2. Đọc dữ liệu từ tệp INPUT.TXT lưu vào mảng 1 chiều a và hiển thị dữ liệu từ mảng a ra màn hình."
     * @param inputPath input file path. 
     * @return an array of float from INPUT.TXT
     */
    private static float[] readInputData(String inputPath) {
        System.out.println("[INFO]: Reading file \"" + inputPath + "\" and display data.");
        ArrayList<Float> arrList = new ArrayList<Float>();

        try {
            File inputFile = new File(inputPath);
            Scanner sc = new Scanner(inputFile);
            while (sc.hasNextLine()) {
              String data = sc.nextLine();
              arrList.add(Float.parseFloat(data));
            }
            sc.close();
        } catch  (FileNotFoundException e) {
            System.out.println("[ERROR]: input file does not exist, please enter data first.");
            return null;
        } catch (Exception e) {
            System.out.println("[ERROR]: an error occured: " + e);
            e.printStackTrace();
        }

        // Convert back to primitive array
        // We can easily pass the number N which is the expected length of the array,
        // however, that will make this func depends on the initialize func. 
        float[] arr = new float[arrList.size()];
        int i = 0;

        for (Float f : arrList) {
            arr[i++] = (f != null ? f : 0.0f);
        }

        System.out.println(arrayToString(arr));
        return arr;
    }

    /**
     * "3. Sắp xếp mảng a sau khi đã đọc được dữ liệu từ bước 2 theo thứ tự tăng dần bằng thuật toán Bubble Sort. 
     * Ghi lại giá trị của mảng a tại mỗi bước của thuật toán và 
     * tự động in ra kết quả màn hình bước cuối cùng rồi lưu vào tệp OUTPUT1.TXT"
     * @param arr array to sort.
     * @param outputPath output file path to export bubble sort result.
     */
    private static void processBubbleSort(float[] arr, String outputPath) {
        System.out.println("[INFO]: running bubble sort.");
        System.out.print("[INFO]: array before bubble sort: ");
        System.out.println(arrayToString(arr));

        System.out.println("[INFO]: the array state through each bubble sort step: ");
        float tmp;
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < arr.length - 1; i++) { 
            for(int j = 0; j < arr.length - 1 - i; j++) { 
                if(arr[j] > arr[j + 1]) { 
                    tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
            String currentState = arrayToString(arr);
            System.out.println(currentState);
            result.append(currentState + "\n");
        }

        publishResult(result.toString(), outputPath, "bubble sort");
    }

    /**
     * "4. Sắp xếp mảng a sau khi đã đọc được dữ liệu từ bước 2 theo thứ tự tăng dần bằng thuật toán Selection Sort. 
     * Ghi lại giá trị của mảng a tại mỗi bước của thuật toán và 
     * tự động in ra kết quả màn hình bước cuối cùng rồi lưu vào tệp OUTPUT2.TXT"
     * @param arr array to sort.
     * @param outputPath output file path to export selection sort result.
     */
    private static void processSelectionSort(float[] arr, String outputPath) {
        System.out.println("[INFO]: performing selection sort.");
        System.out.print("[INFO]: array before selection sort: ");
        System.out.println(arrayToString(arr));
        
        int minIdx;
        StringBuilder result = new StringBuilder();
        System.out.println("[INFO]: the array state through each selection sort step: ");
        for (int i = 0; i < arr.length - 1; i++) {
            minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            float tmp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = tmp;

            String currentState = arrayToString(arr);
            System.out.println(currentState);
            result.append(currentState + "\n");
        }

        publishResult(result.toString(), outputPath, "selection sort");
    }

    /**
     * "5. Sắp xếp mảng a sau khi đã đọc được dữ liệu từ bước 2 theo thứ tự tăng dần bằng thuật toán Insertion Sort. 
     * Ghi lại giá trị của mảng a tại mỗi bước của thuật toán và 
     * tự động in ra kết quả màn hình bước cuối cùng rồi lưu vào tệp OUTPUT3.TXT"
     * @param arr array to sort.
     * @param outputPath output file path to export insertion sort result.
     */
    private static void processInsertionSort(float[] arr, String outputPath) {
        System.out.println("[INFO]: performing insertion sort.");
        System.out.print("[INFO]: array before insertion sort: ");
        System.out.println(arrayToString(arr));

        int idx; 
        float newNumber;
        StringBuilder result = new StringBuilder();
        System.out.println("[INFO]: the array state through each insertion sort step: ");
        for (int i = 1; i < arr.length; i++) {
            idx = i;
            newNumber = arr[i];
            while (idx > 0 && arr[idx - 1] > newNumber) {
                arr[idx] = arr[idx - 1];
                idx--;
            }
            arr[idx] = newNumber;
            String currentState = arrayToString(arr);
            System.out.println(currentState);
            result.append(currentState + "\n");
        }
        
        publishResult(result.toString(), outputPath, "insertion sort");
    }

    /**
     * "6. Sử dụng phương pháp Tìm kiếm Tuyến Tính (Linear Search), 
     * in ra màn hình vị trí của tất cả các phần tử có giá trị lớn hơn value 
     * (value là một số thực được người dùng nhập vào) trong mảng a ở bước 2 (mảng chưa được sắp xếp)."
     * @param arr
     */
    private static void processLinearSearch(float[] arr, Scanner sc, String outputPath) {
        System.out.println("[INFO]: starting linear search.");
        System.out.print("[INFO]: array that's used for linear searching: ");
        System.out.println(arrayToString(arr));

        System.out.print("> Enter the float number to search: ");
        float compareNumber = sc.nextFloat();

        StringBuilder result = new StringBuilder();
        System.out.println("[INFO]: The following elements are greater than the given number:");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > compareNumber) {
                System.out.println("[" + i + "]: " + arr[i]);
                result.append("[" + i + "]: " + arr[i] + "\n");
            }
        }

        publishResult(result.toString(), outputPath, "linear search");
    }

    /**
     * "7. Sử dụng phương pháp Tìm Kiếm Nhị Phân (Binary Search), in ra màn hình vị trí của phần tử đầu tiên 
     * có giá trị bằng với value (value là một số thực được người dùng nhập vào) trong mảng b 
     * (mảng b là mảng a sau khi đã được sắp xếp bằng 1 trong 3 thuật toán ở bước 3,4,5)."
     * @param sortedArr sorted array which will be used for binary search.
     */
    private static void processBinarySearch(float[] sortedArr, Scanner sc, String outputPath) {
        System.out.println("[INFO]: starting binary search.");
        System.out.print("[INFO]: array that's used for binary searching: ");
        System.out.println(arrayToString(sortedArr));

        System.out.print("> Enter the float number to search: ");
        float searchNum = sc.nextFloat();

        int lowerBound = 1;
        int upperBound = sortedArr.length;
        int midIdx;
        boolean foundNum = false;

        while (!foundNum) {
            if (upperBound < lowerBound) {
                System.out.print(">>> Not found such a number in the array.");
                publishResult("Not found " + searchNum + " in the array.", outputPath, "binary search");
                break;
            }

            midIdx = lowerBound + (upperBound - lowerBound) / 2;

            if (sortedArr[midIdx] < searchNum) {
                lowerBound = midIdx + 1;
            }

            if (sortedArr[midIdx] > searchNum) {
                upperBound = midIdx - 1;
            }

            if (sortedArr[midIdx] == searchNum) {
                System.out.println(">>> Found the first match at index = " + midIdx + ".");
                publishResult("Found the first match at index = " + midIdx + ".", outputPath, "binary search");
                foundNum = true;
            }
        }
    }

    /**
     * 
     * @param arr array to sort.
     */
    private static void calculateAlgoExecutionTime(float[] arr) {
        System.out.println("Experiment array: " + arrayToString(arr));
        float[] bubbleSortArray = Arrays.copyOf(arr, arr.length);
        float[] selectionSortArray = Arrays.copyOf(arr, arr.length);
        float[] insertionSortArray = Arrays.copyOf(arr, arr.length);

        long startTime = System.nanoTime();
        float tmp;
        for(int i = 0; i < bubbleSortArray.length - 1; i++) { 
            for(int j = 0; j < bubbleSortArray.length - 1 - i; j++) { 
                if(bubbleSortArray[j] > bubbleSortArray[j + 1]) { 
                    tmp = bubbleSortArray[j];
                    bubbleSortArray[j] = bubbleSortArray[j + 1];
                    bubbleSortArray[j + 1] = tmp;
                }
            }
        }
        long stopTime = System.nanoTime();
        System.out.println("[INFO] Bubble sort execution time = " + (stopTime - startTime));

        long startTime2 = System.nanoTime();
        int minIdx;
        for (int i = 0; i < selectionSortArray.length - 1; i++) {
            minIdx = i;
            for (int j = i + 1; j < selectionSortArray.length; j++) {
                if (selectionSortArray[j] < selectionSortArray[minIdx]) {
                    minIdx = j;
                }
            }
            tmp = selectionSortArray[i];
            selectionSortArray[i] = selectionSortArray[minIdx];
            selectionSortArray[minIdx] = tmp;
        }
        long stopTime2 = System.nanoTime();
        System.out.println("[INFO] Selection sort execution time = " + (stopTime2 - startTime2));

        // insertion sort calculation
        long startTime3 = System.nanoTime();
        int idx; 
        float newNumber;
        for (int i = 1; i < insertionSortArray.length; i++) {
            idx = i;
            newNumber = insertionSortArray[i];
            while (idx > 0 && insertionSortArray[idx - 1] > newNumber) {
                insertionSortArray[idx] = insertionSortArray[idx - 1];
                idx--;
            }
            insertionSortArray[idx] = newNumber;
        }
        long stopTime3 = System.nanoTime();
        System.out.println("[INFO] Insertion sort execution time = " + (stopTime3 - startTime3));
    }

    //endregion 

    //region Helpers, Commons

    private static String arrayToString(float[] arr) {
        StringBuilder arrayAsString = new StringBuilder("[");

        for (int i = 0; i < arr.length; i++) {
            arrayAsString.append(Float.toString(arr[i]));

            if (i != arr.length - 1) {
                arrayAsString.append(" ; ");
            }
        }

        return arrayAsString
            .append("]")
            .toString();
    }

    private static void publishResult(String output, String outputPath, String caller) {
        System.out.println("[INFO]: writing " + caller + " result to \"" + outputPath + "\".");
        try {
            File outputFile = new File(outputPath);
            if (outputFile.createNewFile()) {
                System.out.println("[INFO]: output file is created!");
            }

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputPath));
            outputWriter.write(output);

            outputWriter.flush();
            outputWriter.close();
            System.out.println("[INFO]: success writing " + caller + " result to \"" + outputPath + "\"");
        }
        catch (IOException ex) {
            System.out.println("[ERROR]: an error occured: " + ex);
            ex.printStackTrace();
        }
    }
    //endregion 
}
