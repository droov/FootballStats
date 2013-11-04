package footballStats;

import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm {

  // Method to get the list of unique teams
  public String[] getTeams() throws Exception {
    String[] teams = new String[20];
    ParseCSV pcsv = new ParseCSV();
    pcsv.importData();
    String[][] data = pcsv.getData();
    int check = 0;
    int i, j, n = 0;

    for (i = 0; i < data.length; i++) {
      for (j = 0; j < teams.length; j++) {
        if (data[i][2].equals(teams[j])) {
          check = 1;
          break;
        }
        check = 0;
      }
      if (check == 0) {
        teams[n] = data[i][2];
        n++;
      }
    }

    Arrays.sort(teams);
    return teams;
  }

  // First Algorithm
  // Calculates the confidence, payout and c/p ratio for each betting site across the season
  public String[][] bettingPatterns() throws Exception {
    ParseCSV pcsv = new ParseCSV();
    pcsv.importData();
    String[][] data = pcsv.getData();
    char result = ' ';
    int i, j;
    int count = 0;
    double home, draw, away, payout = 0, confidence, ratio, totalPayout = 0, totalConfidence = 0;

    // Initialising a map that saves the columnar references in the csv. Note: Needs to be modified
    // if source format changes
    String[][] columnMap =
        {
            {"FTR", "B365H", "B365D", "B365A", "BWH", "BWD", "BWA", "GBH", "GBD", "GBA", "IWH",
                "IWD", "IWA", "LBH", "LBD", "LBA", "PSH", "PSD", "PSA", "WHH", "WHD", "WHA", "SJH",
                "SJD", "SJA", "VCH", "VCD", "VCA", "BSH", "BSD", "BSA"},
            {"6", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34",
                "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
                "49", "50", "51"}};
    String[][] bookerRatio = new String[(columnMap[0].length - 1) / 3][4];

    for (j = 1; j < columnMap[0].length; j += 3) {
      home = draw = away = confidence = totalConfidence = totalPayout = ratio = 0;
      for (i = 0; i < data.length; i++) {
        result = data[i][Integer.parseInt(columnMap[1][0])].charAt(0);
        home = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j])]);
        draw = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j + 1])]);
        away = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j + 2])]);

        // Calculating payout
        if (result == 'H')
          payout = home;
        else if (result == 'D')
          payout = draw;
        else
          payout = away;

        // Calculating the confidence
        confidence = calculateConfidence(home, draw, away);

        ratio += confidence / payout;
        totalPayout += payout;
        totalConfidence += confidence;
      }

      // Storing the results
      bookerRatio[count][0] = columnMap[0][j].substring(0, columnMap[0][j].length() - 1);
      bookerRatio[count][1] = Double.toString(totalConfidence / i);
      bookerRatio[count][2] = Double.toString(totalPayout / i);
      bookerRatio[count][3] = Double.toString(ratio / i);
      count++;
    }

    System.out.println("Algorithm 1: `Betting Patterns` succesfully evaluated");
    return bookerRatio;

  }

  // Method to calculate the confidence of the bookie - equal to the difference between the maximum
  // and minimum betting odd
  public double calculateConfidence(double home, double draw, double away) {
    double diff = 0.0;

    if (Math.abs(home - draw) > diff) diff = Math.abs(home - draw);
    if (Math.abs(home - away) > diff) diff = Math.abs(home - away);
    if ((Math.abs(draw - away) > diff)) diff = Math.abs(draw - away);
    return diff;
  }

  // Second Algorithm
  // Calculates the confidence, payout and c/p ratio for each betting site split by team across the
  // season
  public String[][] bettingTeamWise() throws Exception {
    ParseCSV pcsv = new ParseCSV();
    pcsv.importData();
    String[][] data = pcsv.getData();
    char result = ' ';
    int i, j, k, games, count = 0;
    double home, draw, away, payout, confidence, ratio, totalPayout = 0, totalConfidence = 0;

    // Initialising a map that saves the columnar references in the csv. Note: Needs to be modified
    // if source format changes
    String[][] columnMap =
        {
            {"FTR", "B365H", "B365D", "B365A", "BWH", "BWD", "BWA", "GBH", "GBD", "GBA", "IWH",
                "IWD", "IWA", "LBH", "LBD", "LBA", "PSH", "PSD", "PSA", "WHH", "WHD", "WHA", "SJH",
                "SJD", "SJA", "VCH", "VCD", "VCA", "BSH", "BSD", "BSA"},
            {"6", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34",
                "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
                "49", "50", "51"}};

    String[] teamList = getTeams();
    String[][] resultList = new String[((columnMap[0].length - 1) / 3) * teamList.length][5];

    // Similar logic implied for evaluating betting patterns but split by team
    for (k = 0; k < teamList.length; k++) {
      for (j = 1; j < columnMap[0].length; j += 3) {
        home = draw = ratio = away = confidence = totalConfidence = totalPayout = games = 0;
        for (i = 0; i < data.length; i++) {
          if (data[i][2].equalsIgnoreCase(teamList[k]) || data[i][3].equalsIgnoreCase(teamList[k])) {
            result = data[i][Integer.parseInt(columnMap[1][0])].charAt(0);
            home = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j])]);
            draw = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j + 1])]);
            away = Double.parseDouble(data[i][Integer.parseInt(columnMap[1][j + 2])]);

            if (result == 'H')
              payout = home;
            else if (result == 'D')
              payout = draw;
            else
              payout = away;
            confidence = calculateConfidence(home, draw, away);

            ratio += confidence / payout;
            totalConfidence += confidence;
            totalPayout += payout;
            games++;
          } else
            continue;
        }

        resultList[count][0] = teamList[k];
        resultList[count][1] = columnMap[0][j].substring(0, columnMap[0][j].length() - 1);
        resultList[count][2] = Double.toString(totalConfidence / games);
        resultList[count][3] = Double.toString(totalPayout / games);
        resultList[count][4] = Double.toString(ratio / games);
        count++;
      }
    }

    System.out.println("Algorithm 2: `Betting Patterns Team Wise` successfully evaluated");
    return resultList;
  }

  public String[][] homeAwayPerformance() throws Exception {
    ParseCSV pcsv = new ParseCSV();
    pcsv.importData();
    String[][] data = pcsv.getData();
    String[] teams = getTeams();
    int i, j;
    double positivePerformanceValue = 0;
    double negativePerformanceValue = 0;
    double netPerformanceValue = 0;
    int checkHome = 0;
    int checkAway = 0;

    String[][] columnMap =
        { {"HS", "AS", "HST", "AST", "HF", "AF", "HC", "AC", "HY", "AY", "HR", "AR"},
            {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"}};

    String[][] performance = new String[teams.length][7];

    for (i = 0; i < teams.length; i++) {
      performance[i][0] = teams[i];
    }

    for (i = 0; i < performance.length; i++) {
      for (j = 1; j < performance[i].length; j++) {
        performance[i][j] = "0";
      }
    }

    // Evaluating Algorithm
    for (i = 0; i < teams.length; i++) {
      for (j = 0; j < data.length; j++) {
        if (data[j][2].equals(teams[i]) || data[j][3].equals(teams[i])) {
          if (data[j][2].equals(teams[i])) {
            // If it is at home
            positivePerformanceValue = Double.parseDouble(performance[i][1]);
            negativePerformanceValue = Double.parseDouble(performance[i][2]);
            netPerformanceValue = Double.parseDouble(performance[i][3]);
            if (checkHome == 0) {
              positivePerformanceValue =
                  0.5 * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][2])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][0])]) + 0.2
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][6])]);
              negativePerformanceValue =
                  0.5 * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][10])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][8])]) + 0.2
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][4])]);
              netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
              checkHome = 1;
            } else {
              positivePerformanceValue =
                  (positivePerformanceValue + (0.5
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][2])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][0])]) + 0.2 * Double
                      .parseDouble(data[j][Integer.parseInt(columnMap[1][6])]))) / 2;
              negativePerformanceValue =
                  (positivePerformanceValue + (0.5
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][10])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][8])]) + 0.2 * Double
                      .parseDouble(data[j][Integer.parseInt(columnMap[1][4])]))) / 2;
              netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
            }
            performance[i][1] = String.valueOf(positivePerformanceValue);
            performance[i][2] = String.valueOf(negativePerformanceValue);
            performance[i][3] = String.valueOf(netPerformanceValue);

          } else {
            // If it is away
            positivePerformanceValue = Double.parseDouble(performance[i][4]);
            negativePerformanceValue = Double.parseDouble(performance[i][5]);
            netPerformanceValue = Double.parseDouble(performance[i][6]);
            if (checkAway == 0) {
              positivePerformanceValue =
                  0.5 * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][3])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][1])]) + 0.2
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][7])]);
              negativePerformanceValue =
                  0.5 * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][11])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][9])]) + 0.2
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][5])]);
              netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
              checkAway = 1;
            } else {
              positivePerformanceValue =
                  (positivePerformanceValue + (0.5
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][3])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][1])]) + 0.2 * Double
                      .parseDouble(data[j][Integer.parseInt(columnMap[1][7])]))) / 2;
              negativePerformanceValue =
                  (positivePerformanceValue + (0.5
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][11])]) + 0.3
                      * Double.parseDouble(data[j][Integer.parseInt(columnMap[1][9])]) + 0.2 * Double
                      .parseDouble(data[j][Integer.parseInt(columnMap[1][5])]))) / 2;
              netPerformanceValue = positivePerformanceValue - negativePerformanceValue;
            }
            performance[i][4] = String.valueOf(positivePerformanceValue);
            performance[i][5] = String.valueOf(negativePerformanceValue);
            performance[i][6] = String.valueOf(netPerformanceValue);
          }
        }
      }
      checkHome = 0;
      checkAway = 0;
    }

    System.out.println("Algorithm 3: `Home and Away Performance` successfully evaluated");
    return performance;
  }

  public String[][] halfFullRelation() throws Exception {
    ParseCSV pcsv = new ParseCSV();
    pcsv.importData();
    String[][] data = pcsv.getData();
    String[][] columnMap = { {"FTR", "HTR"}, {"6", "9"}};
    String[][] relation = new String[20][3];
    String[] teams = getTeams();
    int i, j;
    double homeTurnArounds = 0;
    double awayTurnArounds = 0;

    for (i = 0; i < teams.length; i++) {
      relation[i][0] = teams[i];
    }

    for (i = 0; i < relation.length; i++) {
      for (j = 1; j < relation[i].length; j++) {
        relation[i][j] = "0";
      }
    }

    // Evaluating the algorithm
    for (i = 0; i < teams.length; i++) {
      for (j = 0; j < data.length; j++) {
        if (data[j][2].equals(teams[i]) || data[j][3].equals(teams[i])) {
          if (data[j][2].equals(teams[i])) {
            // For home
            homeTurnArounds = Double.parseDouble(relation[i][1]);
            if (data[j][Integer.parseInt(columnMap[1][1])].equals("H")) {
              if (data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
                homeTurnArounds -= 1;
              } else {
                if (data[j][Integer.parseInt(columnMap[1][0])].equals("D")) {
                  homeTurnArounds -= 0.5;
                }
              }
            }
            if (data[j][Integer.parseInt(columnMap[1][1])].equals("A")) {
              if (data[j][Integer.parseInt(columnMap[1][0])].equals("H")) {
                homeTurnArounds += 1;
              } else {
                if (data[j][Integer.parseInt(columnMap[1][0])].equals("D")) {
                  homeTurnArounds += 0.5;
                }
              }
            }
            if (data[j][Integer.parseInt(columnMap[1][1])].equals("D")) {
              if (data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
                homeTurnArounds -= 0.5;
              } else {
                if (data[j][Integer.parseInt(columnMap[1][0])].equals("H")) {
                  homeTurnArounds += 0.5;
                }
              }
            }
            relation[i][1] = String.valueOf(homeTurnArounds);

          } else {
            // for away
            awayTurnArounds = Double.parseDouble(relation[i][2]);
            if (data[j][Integer.parseInt(columnMap[1][1])].equals("H")) {
              if (data[j][Integer.parseInt(columnMap[1][0])].equals("D")) {
                awayTurnArounds += 0.5;
              } else {
                if (data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
                  awayTurnArounds += 1;
                }
              }
            }
            if (data[j][Integer.parseInt(columnMap[1][1])].equals("A")) {
              if (data[j][Integer.parseInt(columnMap[1][0])].equals("H")) {
                awayTurnArounds -= 1;
              } else {
                if (data[j][Integer.parseInt(columnMap[1][0])].equals("D")) {
                  awayTurnArounds -= 0.5;
                }
              }
            }
            if (data[j][Integer.parseInt(columnMap[1][1])].equals("D")) {
              if (data[j][Integer.parseInt(columnMap[1][0])].equals("A")) {
                awayTurnArounds += 0.5;
              } else {
                if (data[j][Integer.parseInt(columnMap[1][0])].equals("H")) {
                  awayTurnArounds -= 0.5;
                }
              }
            }
            relation[i][2] = String.valueOf(awayTurnArounds);
          }
        }
      }
    }

    System.out.println("Algorithm 4: `Half Time Full Time Relation` successfully evaluated");
    return relation;
  }
}