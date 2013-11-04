package footballStats;

import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm {
	
	public String[] getTeams() throws Exception {
		String[] teams = new String[20];
		
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
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
	
	public String[][] bettingPatterns() throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		char result = ' ';
		int i,j;
		int count = 0;
		double home, draw, away, payout = 0, difference, ratio, totalPayout=0, totalDifference=0;
				
		String[][] columnMap = { { "FTR", "B365H", "B365D", "B365A", "BWH", "BWD", "BWA", "GBH", "GBD", "GBA", "IWH", "IWD", "IWA", "LBH", "LBD", "LBA", "PSH", "PSD", "PSA", "WHH", "WHD", "WHA", "SJH", "SJD", "SJA", "VCH", "VCD", "VCA", "BSH", "BSD", "BSA" }, { "6", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51" } };
		String[][]bookerRatio = new String[(columnMap[0].length-1)/3][4];
		
		for (j = 1; j < columnMap[0].length; j+=3) {
			home = draw = away = difference = totalDifference = totalPayout = ratio = 0;
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
				totalPayout += payout;
				totalDifference += difference;
			}			
			//bookerRatio.put(columnMap[0][j].substring(0, columnMap[0][j].length() - 1), ratio/i);
			bookerRatio[count][0] = columnMap[0][j].substring(0, columnMap[0][j].length() - 1);
			bookerRatio[count][1] = Double.toString(totalDifference/i);
			bookerRatio[count][2] = Double.toString(totalPayout/i);
			bookerRatio[count][3] = Double.toString(ratio/i);
			count++;
		}		
		
		for(i=0;i<bookerRatio.length;i++){
			for(j=0;j<bookerRatio[0].length;j++)
				System.out.print(bookerRatio[i][j] + " ");
			System.out.println();
		}
		
		return bookerRatio;
		
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
	
	public String[][] bettingTeamWise()throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		char result = ' ';
		int i,j,k,games,count=0;
		double home, draw, away, payout, difference, ratio, totalPayout=0, totalDifference=0;
				
		String[][] columnMap = { { "FTR", "B365H", "B365D", "B365A", "BWH", "BWD", "BWA", "GBH", "GBD", "GBA", "IWH", "IWD", "IWA", "LBH", "LBD", "LBA", "PSH", "PSD", "PSA", "WHH", "WHD", "WHA", "SJH", "SJD", "SJA", "VCH", "VCD", "VCA", "BSH", "BSD", "BSA" }, { "6", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51" } };
		String[] teamList = getTeams();
		
		String[][]resultList = new String[((columnMap[0].length-1)/3)*teamList.length][5];
		System.out.println(resultList.length);
		System.out.println(resultList[0].length);
		
		
		for(k = 0; k < teamList.length; k++){			
			for (j = 1; j < columnMap[0].length; j+=3) {
				home = draw = ratio = away = difference = totalDifference = totalPayout = games =  0;
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
						totalDifference += difference;
						totalPayout += payout;
						games++;
					}
					else 
						continue;
				}
				
				resultList[count][0] = teamList[k];
				resultList[count][1] = columnMap[0][j].substring(0, columnMap[0][j].length() - 1);
				resultList[count][2] = Double.toString(totalDifference/games);
				resultList[count][3] = Double.toString(totalPayout/games);
				resultList[count][4] = Double.toString(ratio/games);
				count++;
			}
		}
		for(int m=0;m<resultList.length;m++){
			for(int n=0;n<resultList[0].length;n++)
				System.out.print(resultList[m][n] + "\t");
			System.out.println();
		}
		return resultList;
	}
	
	public String[][] homeAwayPerformance() throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		
		String[] teams = getTeams();
		
		String[][] columnMap = { { "HS", "AS", "HST" , "AST" , "HF" , "AF" , "HC" , "AC" , "HY" , "AY" , "HR" , "AR"}, { "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" } };
		
		String[][] Performance = new String[20][7];
		
		int i, j;
		
		for( i = 0 ; i < teams.length ; i++ ) {
			Performance[i][0] = teams[i];
		}
		
		for( i = 0 ; i < Performance.length ; i++ ) {
			for( j = 1 ; j < Performance[i].length ; j++) {
				Performance[i][j] = "0";
			}
		}
		
		double positivePerformanceValue = 0;
		double negativePerformanceValue = 0;
		double netPerformanceValue = 0;
		int k;
		int checkHome = 0;
		int checkAway = 0;
		
		for (i = 0 ; i < teams.length ; i++ ) {
			for (j = 0 ; j < data.length ; j++ ) {
				if (data[j][2].equals(teams[i]) || data[j][3].equals(teams[i])) {
					
					if(data[j][2].equals(teams[i])) {
						//If it is at home
						positivePerformanceValue = Double.parseDouble(Performance[i][1]);
						negativePerformanceValue = Double.parseDouble(Performance[i][2]);
						netPerformanceValue = Double.parseDouble(Performance[i][3]);
						if(checkHome == 0) {
							positivePerformanceValue = 0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][2])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][0])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][6])]);
							negativePerformanceValue = 0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][10])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][8])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][4])]);
							netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
							checkHome = 1;
						}
						else {
							positivePerformanceValue = (positivePerformanceValue + (0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][2])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][0])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][6])])))/2;
							negativePerformanceValue = (positivePerformanceValue + (0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][10])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][8])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][4])])))/2;
							netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
						}
						Performance[i][1] = String.valueOf(positivePerformanceValue);
						Performance[i][2] = String.valueOf(negativePerformanceValue);
						Performance[i][3] = String.valueOf(netPerformanceValue);
						
						
					}
					else {
						//If it is away
						positivePerformanceValue = Double.parseDouble(Performance[i][4]);
						negativePerformanceValue = Double.parseDouble(Performance[i][5]);
						netPerformanceValue = Double.parseDouble(Performance[i][6]);
						if(checkAway == 0) {
							positivePerformanceValue = 0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][3])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][1])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][7])]);
							negativePerformanceValue = 0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][11])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][9])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][5])]);
							netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
							checkAway = 1;
						}
						else {
							positivePerformanceValue = (positivePerformanceValue + (0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][3])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][1])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][7])])))/2;
							negativePerformanceValue = (positivePerformanceValue + (0.5*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][11])]) + 0.3*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][9])]) + 0.2*Double.parseDouble(data[j][Integer.parseInt(columnMap[1][5])])))/2;
							netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
						}
						Performance[i][4] = String.valueOf(positivePerformanceValue);
						Performance[i][5] = String.valueOf(negativePerformanceValue);
						Performance[i][6] = String.valueOf(netPerformanceValue);
					}
				}
			}
			checkHome = 0;
			checkAway = 0;
		}
		
		for( i = 0 ; i < Performance.length ; i++ ) {
			for( j = 0 ; j < Performance[i].length ; j++) {
				System.out.print(Performance[i][j] + " ");
			}
			System.out.println();
		}
		
		
		return Performance;
	}
	
	public String[][] halfFullRelation() throws Exception {
		ParseCSV pcsv = new ParseCSV();
		pcsv.importData();
		ArrayList<String> headers = pcsv.getHeaders();
		String[][] data = pcsv.getData();
		
		String[][] columnMap = { { "FTR", "HTR" }, { "6", "9" } };
		
		String[][] relation = new String[20][3];
		
		String[] teams = getTeams();
		
		int i, j;
		double homeTurnArounds = 0;
		double awayTurnArounds = 0;
		
		for( i = 0 ; i < teams.length ; i++ ) {
			relation[i][0] = teams[i];
		}
		
		for( i = 0 ; i < relation.length ; i++ ) {
			for( j = 1 ; j < relation[i].length ; j++) {
				relation[i][j] = "0";
			}
		}
		
		
		for (i = 0 ; i < teams.length ; i++ ) {
			for (j = 0 ; j < data.length ; j++ ) {
				if (data[j][2].equals(teams[i]) || data[j][3].equals(teams[i])) {
					if(data[j][2].equals(teams[i])) {
						//If it is at home
						homeTurnArounds = Double.parseDouble(relation[i][1]);
						if(data[j][Integer.parseInt(columnMap[1][1])].equals("H")) {
							if(data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
								homeTurnArounds -= 1;
							}
							else {
								if(data[j][Integer.parseInt(columnMap[1][0])].equals("D")){
									homeTurnArounds -= 0.5;
								}
							}
						}
						if(data[j][Integer.parseInt(columnMap[1][1])].equals("A")) {
							if(data[j][Integer.parseInt(columnMap[1][0])].equals("H")) {
								homeTurnArounds += 1;
							}
							else {
								if(data[j][Integer.parseInt(columnMap[1][0])].equals("D")){
									homeTurnArounds += 0.5;
								}
							}
						}
						if(data[j][Integer.parseInt(columnMap[1][1])].equals("D")) {
							if(data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
								homeTurnArounds -= 0.5;
							}
							else {
								if(data[j][Integer.parseInt(columnMap[1][0])].equals("H")){
									homeTurnArounds += 0.5;
								}
							}
						}
						relation[i][1] = String.valueOf(homeTurnArounds);
						
					}
					else {
						//for away
						awayTurnArounds = Double.parseDouble(relation[i][2]);
						if(data[j][Integer.parseInt(columnMap[1][1])].equals("H")) {
							if(data[j][Integer.parseInt(columnMap[1][0])].equals("D")) {
								awayTurnArounds += 0.5;
							}
							else {
								if(data[j][Integer.parseInt(columnMap[1][0])].equals("A")){
									awayTurnArounds += 1;
								}
							}
						}
						if(data[j][Integer.parseInt(columnMap[1][1])].equals("A")) {
							if(data[j][Integer.parseInt(columnMap[1][0])].equals("H")) {
								awayTurnArounds -= 1;
							}
							else {
								if(data[j][Integer.parseInt(columnMap[1][0])].equals("D")){
									awayTurnArounds -= 0.5;
								}
							}
						}
						if(data[j][Integer.parseInt(columnMap[1][1])].equals("D")) {
							if(data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
								awayTurnArounds += 0.5;
							}
							else {
								if(data[j][Integer.parseInt(columnMap[1][0])].equals("H")){
									awayTurnArounds -= 0.5;
								}
							}
						}
						relation[i][2] = String.valueOf(awayTurnArounds);
					}
				}
			}
		}
		for( i = 0 ; i < relation.length ; i++ ) {
			for( j = 0 ; j < relation[i].length ; j++) {
				System.out.print(relation[i][j] + " ");
			}
			System.out.println();
		}	
			
		return relation;
	}
}
