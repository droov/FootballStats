package footballStats;

import java.util.*;
import java.io.*;

public class ParseCSV {

  private ArrayList<String> headerList = new ArrayList(); // stores the headers contained in the csv
  private String[][] data; // stores the data in the csv

  // Get methods
  public ArrayList<String> getHeaders() {
    return headerList;
  }

  public String[][] getData() {
    return data;
  }

  public void importData() throws Exception {
    String filename = "resources/201213.csv"; // csv file with english football league data that is
                                              // being read
    Scanner sc = new Scanner(new File(filename));
    sc.useDelimiter(",");
    String line = "", header = "";
    int i = 0;
    int numberOfRows = 0;

    numberOfRows = numberOfRows(filename);
    header = sc.nextLine(); // reading the header line
    StringTokenizer head = new StringTokenizer(header);

    while (head.hasMoreTokens())
      headerList.add(head.nextToken(",")); // storing the headers

    data = new String[numberOfRows][headerList.size()];
    StringTokenizer str;

    for (i = 0; i < numberOfRows; i++) {
      line = sc.nextLine();
      str = new StringTokenizer(line);
      for (int j = 0; j < data[0].length; j++) {
        if (str.hasMoreTokens()) data[i][j] = str.nextToken(","); // storing the data in the array
      }
    }
  }

  // Method to return the number of rows in the csv file being read
  public static int numberOfRows(String filename) throws Exception {
    Scanner sc = new Scanner(new File(filename));
    sc.nextLine();
    int count = 0;

    while (sc.hasNext()) {
      sc.nextLine();
      count++;
    }
    return count;
  }

}
