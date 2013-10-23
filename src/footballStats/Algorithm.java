package footballStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Algorithm {
	
	public String[] getTeams() throws Exception {
		String[] teams = new String[20];
		
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		int check = 0;
		int i, j, n;
		n = 0;
		for(i = 0 ; i < data.length ; i++ ) {
			for(j = 0; j < teams.length ; j++) {
				if(data[i][2].equals(teams[j])) {
					check = 1;
					break;
				}
				check = 0;
			}
			if(check == 0) {
				teams[n] = data[i][2];
				n++;
			}
		}
		
		Arrays.sort(teams);
		
		return teams;
	}
	
	public void bettingPatterns() throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		char result = ' ';
		int i,j;
		double home, draw, away, payout, difference, ratio;
		
		LinkedHashMap<String, Double> bookerRatio = new LinkedHashMap<String, Double>();
		String[][] columnMap = { { "FTR", "B365H", "B365D", "B365A", "BWH", "BWD", "BWA", "GBH", "GBD", "GBA", "IWH", "IWD", "IWA", "LBH", "LBD", "LBA", "PSH", "PSD", "PSA", "WHH", "WHD", "WHA", "SJH", "SJD", "SJA", "VCH", "VCD", "VCA", "BSH", "BSD", "BSA" }, { "6", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51" } };

		for (j = 1; j < columnMap[0].length; j+=3) {
			home = draw = away = difference = ratio = 0;
			for (i = 0; i < data.length; i++) {
				result = data[i][Integer.parseInt(columnMap[1][0])].charAt(0);	
				home = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j])]);
				draw = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j+1])]);
				away = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j+2])]);
				
				if(result == 'H')
					payout = home;
				else if(result == 'D')
					payout = draw;
				else
					payout = away;
				difference = calculateDifference(home, draw, away);
				
				ratio += difference / payout;				
				
			}
			bookerRatio.put(columnMap[0][j].substring(0, columnMap[0][j].length() - 1), ratio/i);
		}
		for (Entry<String, Double> entry : bookerRatio.entrySet()) {
		    System.out.println(entry.getKey() + " -> " + entry.getValue());
		}
	}
	
	public double calculateDifference(double home, double draw, double away){
		double diff = 0.0;
		
		if(Math.abs(home-draw)>diff)
			diff = Math.abs(home-draw);
		if(Math.abs(home-away)>diff) 
			diff = Math.abs(home-away);
		if((Math.abs(draw-away)>diff))
			diff = Math.abs(draw-away);
		return diff;
	}
	
	public void bettingTeamWise()throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		char result = ' ';
		int i,j,k,games,count=0;
		double home, draw, away, payout, difference, ratio;
				
		String[][] columnMap = { { "FTR", "B365H", "B365D", "B365A", "BWH", "BWD", "BWA", "GBH", "GBD", "GBA", "IWH", "IWD", "IWA", "LBH", "LBD", "LBA", "PSH", "PSD", "PSA", "WHH", "WHD", "WHA", "SJH", "SJD", "SJA", "VCH", "VCD", "VCA", "BSH", "BSD", "BSA" }, { "6", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51" } };
		String[] teamList = getTeams();
		
		String[][]resultList = new String[((columnMap[0].length-1)/3)*teamList.length][3];
		System.out.println(resultList.length);
		System.out.println(resultList[0].length);
		
		
		for(k = 0; k < teamList.length; k++){			
			for (j = 1; j < columnMap[0].length; j+=3) {
				home = draw = ratio = away = difference = games = 0;
				for (i = 0; i < data.length; i++) {
					if ( data[i][2].equalsIgnoreCase(teamList[k]) || data[i][3].equalsIgnoreCase(teamList[k])) { // checking if the team to be evaluated is either the home or the away team
						result = data[i][Integer.parseInt(columnMap[1][0])].charAt(0);	
						home = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j])]);
						draw = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j+1])]);
						away = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j+2])]);
						
						if(result == 'H')
							payout = home;
						else if(result == 'D')
							payout = draw;
						else
							payout = away;
						difference = calculateDifference(home, draw, away);
						
						ratio += difference / payout;
						games++;
					}
					else 
						continue;
				}
				
				resultList[count][0] = teamList[k];
				resultList[count][1] = columnMap[0][j].substring(0, columnMap[0][j].length() - 1);
				resultList[count][2] = Double.toString(ratio/games);
				count++;
				//bookerRatio.put(columnMap[0][j].substring(0, columnMap[0][j].length() - 1), ratio/games);
			}
		}
		for(int m=0;m<resultList.length;m++){
			for(int n=0;n<resultList[0].length;n++)
				System.out.print(resultList[m][n] + "\t");
			System.out.println();
		}
	}
}
