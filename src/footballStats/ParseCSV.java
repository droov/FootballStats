package footballStats;

import java.util.*;
import java.io.*;
import java.text.*;

public class ParseCSV {

	private ArrayList<String> headerList = new ArrayList();
	private String[][] data;

	public ArrayList<String> getHeaders() {
		return headerList;
	}

	public String[][] getData() {
		return data;
	}

	public void importData() throws Exception {
		String filename = "D:/temp/201314.csv";
		Scanner sc = new Scanner(new File(filename));
		sc.useDelimiter(",");
		String line = "", header = "";
		int numberOfRows = 0;

		numberOfRows = numberOfRows(filename);
		//System.out.println("number of rows " + numberOfRows);

		header = sc.nextLine();

		StringTokenizer head = new StringTokenizer(header);

		while (head.hasMoreTokens())
			headerList.add(head.nextToken(","));

		//System.out.println("header size or number of columns: " + headerList.size());

		int i = 0;

		data = new String[numberOfRows][headerList.size()];
		String token = "";
		StringTokenizer str;

		//System.out.println("number of coulmns " + data[0].length);

		for (i = 0; i < numberOfRows; i++) {
			line = sc.nextLine();
			str = new StringTokenizer(line);
			for (int j = 0; j < data[0].length; j++) {
				if (str.hasMoreTokens())
					data[i][j] = str.nextToken(",");

			}

		}

		/*for (i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++)
				System.out.print(data[i][j] + " ");
			System.out.println();
		}*/

	}// main

	public static int numberOfRows(String filename) throws Exception {

		Scanner sc = new Scanner(new File(filename));
		sc.nextLine();
		int count = 0;

		while (sc.hasNext()) {
			sc.nextLine();
			count++;
		}
		return count;
	}// numberOfRows

}// class