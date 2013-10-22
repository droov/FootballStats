package footballStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Algorithm {
	public void bettingPatterns() throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();

		HashMap<String, Integer> columnMap = new HashMap<String, Integer>();
		// Adding relevant columns for analysing betting patterns
		columnMap.put("FTR", 6);
		columnMap.put("B365H", 22);
		columnMap.put("B365D", 23);
		columnMap.put("B365A", 24);

		for (Entry<String, Integer> e : columnMap.entrySet()) {
			System.out.println(e.getKey());
			System.out.println(e.getValue());
		}
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				
			}
		}
	}
}
