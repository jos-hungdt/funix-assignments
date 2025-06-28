import java.util.InputMismatchException;
import java.util.Scanner;

public class vul7_fixed {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Please fill the array");
    System.out.print("Number of element: ");
    int n;
    try {
      n = scanner.nextInt();
      if (n <= 0) {
        System.out.println("Number of elements must be positive");
        scanner.close();
        return;
      }
    } catch (InputMismatchException e) {
      System.out.println("Invalid input: please enter a valid integer");
      scanner.close();
      return;
    }

    byte[] input = new byte[n];
    for (int i = 0; i < n; i++) {
      System.out.print("Element " + i + ": ");
      try {
        input[i] = scanner.nextByte();
      } catch (InputMismatchException e) {
        System.out.println("Invalid input: please enter a valid byte (-128 to 127)");
        scanner.close();
        return;
      }
    }

    System.out.println("Extract data from array");
    System.out.print("Start index: ");
    int start;
    try {
      start = scanner.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Invalid input: please enter a valid integer");
      scanner.close();
      return;
    }
        
    System.out.print("End index: ");
    int end;
    try {
      end = scanner.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Invalid input: please enter a valid integer");
      scanner.close();
      return;
    }

    if (start < 0 || end > input.length || start > end) {
      System.out.println("Invalid index range");
      scanner.close();
      return;
    }
    
    byte[] subData = subArray(input, start, end);
    
    System.out.print("Result: ");
    
    for(int i = 0; i < subData.length; i++) {
      System.out.print("Element " + i + ": " + subData[i]);
    }

    scanner.close();
  }

  public static byte[] subArray(byte[] source, int beginIdx, int endIdx) {
    byte[] result = new byte[endIdx - beginIdx];
    int k = 0;
    
    for (int i = beginIdx; i < endIdx; i++) {
      result[k] = source[i];
      k++;
    }

    return result;
  }
}
