import java.util.Scanner;

import javax.crypto.spec.HKDFParameterSpec.Extract;

public class vul7 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Please fill the array");
    System.out.print("Number of element: ");
    int n = scanner.nextInt();

    byte[] input = new byte[n];
    for (int i = 0; i < n; i++) {
      System.out.print("Element " + i + ": ");
      input[i] = scanner.nextByte();
    }

    System.out.println("Extract data from array");
    System.out.print("Start index: ");
    int start = scanner.nextInt();
    
    System.out.print("End index: ");
    int end = scanner.nextInt();
    
    byte[] subData = subArray(input, start, end);
    
    System.out.print("Result: ");
    
    for(int i = 0; i < subData.length; i++) {
      System.out.print("Element " + i + ": " + subData[i]);
    }
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
