package footballStats;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

public class ResultOutput {

  private String outputFolder = "output/Results.xlsx";

  public void excelOut() throws Exception {
    Algorithm a = new Algorithm();
    int i, j;
    int rownum = 0, cellnum;
    int noBetSites = 10;
    int count = 0;

    XSSFWorkbook workbook = new XSSFWorkbook();
    CreationHelper createHelper = workbook.getCreationHelper();

    // Defining the cell style for the numeric fields
    XSSFCellStyle style = workbook.createCellStyle();
    style.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));

    // Defining the font style for the header
    XSSFFont headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setUnderline(XSSFFont.U_SINGLE);
    XSSFCellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFont(headerFont);
    headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

    /*
     * Algorithm 1: Betting Site Performance
     */
    XSSFSheet sheet1 = workbook.createSheet("BettingPatterns");
    String[][] firstDataSet = a.bettingPatterns();

    // Writing Column headers
    XSSFRow header = sheet1.createRow(rownum++);
    XSSFCell headerCell = header.createCell(0);
    headerCell.setCellValue("Betting Site");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(1);
    headerCell.setCellValue("Confidence");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(2);
    headerCell.setCellValue("Payout");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(3);
    headerCell.setCellValue("D/P Ratio");
    headerCell.setCellStyle(headerStyle);

    // Writing Cell Data
    for (i = 0; i < firstDataSet.length; i++) {
      XSSFRow row = sheet1.createRow(rownum++);
      cellnum = 0;
      for (j = 0; j < firstDataSet[0].length; j++) {
        XSSFCell cell = row.createCell(cellnum++);
        if (j != 0) {
          cell.setCellValue(Double.parseDouble(firstDataSet[i][j]));
          cell.setCellStyle(style);
          cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else
          cell.setCellValue(firstDataSet[i][j]);
      }
    }

    // Creating data sets for chart
    XYSeriesCollection dataset1 = new XYSeriesCollection();
    for (i = 0; i < firstDataSet.length; i++) {
      XYSeries series = new XYSeries(firstDataSet[i][0]);
      series.add(Double.parseDouble(firstDataSet[i][1].substring(0, 4)),
          Double.parseDouble(firstDataSet[i][2].substring(0, 4)));
      dataset1.addSeries(series);
    }

    // Creating chart
    JFreeChart scatterChart =
        ChartFactory.createScatterPlot("Betting Patterns", "Confidence", "Payout", dataset1,
            PlotOrientation.HORIZONTAL, true, true, false);
    scatterChart.getLegend().setPosition(RectangleEdge.RIGHT);
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(2);
    XYPlot plot = (XYPlot) scatterChart.getPlot();
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
    XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{0}");
    renderer.setBaseItemLabelGenerator(generator);
    renderer.setBaseItemLabelsVisible(true);

    // Converting Chart to Image and writing to excel
    ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
    ChartUtilities.writeChartAsJPEG(chartOut, scatterChart, 500, 400);
    int id = workbook.addPicture(chartOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
    chartOut.close();
    XSSFDrawing drawing = sheet1.createDrawingPatriarch();
    ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 0, 15, 10);
    XSSFPicture pic = drawing.createPicture(anchor, id);
    pic.resize();

    // Autofitting Column Width
    for (j = 0; j < firstDataSet[0].length; j++)
      sheet1.autoSizeColumn(j);

    /*
     * Algorithm 2: Betting Site Performance Team Wise Split
     */
    XSSFSheet sheet2 = workbook.createSheet("BettingPatternsTeamWise");
    String[][] secondDataSet = a.bettingTeamWise();
    rownum = 0;

    // Writing Column headers
    header = sheet2.createRow(rownum++);
    headerCell = header.createCell(0);
    headerCell.setCellValue("Team");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(1);
    headerCell.setCellValue("Betting Site");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(2);
    headerCell.setCellValue("Confidence");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(3);
    headerCell.setCellValue("Payout");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(4);
    headerCell.setCellValue("D/P Ratio");
    headerCell.setCellStyle(headerStyle);

    // Writing Cell Data
    for (i = 0; i < secondDataSet.length; i++) {
      XSSFRow row = sheet2.createRow(rownum++);
      cellnum = 0;
      for (j = 0; j < secondDataSet[0].length; j++) {
        XSSFCell cell = row.createCell(cellnum++);
        if (j > 1) {
          cell.setCellValue(Double.parseDouble(secondDataSet[i][j]));
          cell.setCellStyle(style);
          cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else
          cell.setCellValue(secondDataSet[i][j]);
      }
    }

    // Creating data sets for chart
    DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
    for (i = 0; i < secondDataSet.length; i += 10) {
      for (j = 0; j < noBetSites; j++) {
        dataset2.setValue(Double.parseDouble(secondDataSet[count++][4]), secondDataSet[i + j][1],
            secondDataSet[i][0]);
      }

    }

    // Converting Chart to Image and writing to excel
    JFreeChart stackedChart =
        ChartFactory.createStackedBarChart("Betting Site Team Wise Performance", "Team",
            "Betting Website", dataset2, PlotOrientation.HORIZONTAL, true, true, false);
    stackedChart.getLegend().setPosition(RectangleEdge.RIGHT);
    chartOut = new ByteArrayOutputStream();
    ChartUtilities.writeChartAsJPEG(chartOut, stackedChart, 1000, 1800);
    id = workbook.addPicture(chartOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
    chartOut.close();
    drawing = sheet2.createDrawingPatriarch();
    anchor = drawing.createAnchor(0, 0, 0, 0, 6, 0, 15, 10);
    pic = drawing.createPicture(anchor, id);
    pic.resize();

    // Autofitting Column Width
    for (j = 0; j < secondDataSet[0].length; j++)
      sheet2.autoSizeColumn(j);

    /*
     * Algorithm 3: Home vs Away Team Performance
     */
    XSSFSheet sheet3 = workbook.createSheet("HomeAwayPerformance");
    String[][] thirdDataSet = a.homeAwayPerformance();
    rownum = 0;

    // Writing Column headers
    XSSFRow header1 = sheet3.createRow(rownum++);
    headerCell = header1.createCell(0);
    headerCell.setCellValue("Team");
    headerCell.setCellStyle(headerStyle);
    headerCell = header1.createCell(1);
    headerCell.setCellValue("Home");
    headerCell.setCellStyle(headerStyle);
    headerCell = header1.createCell(4);
    headerCell.setCellValue("Away");
    headerCell.setCellStyle(headerStyle);
    XSSFRow header2 = sheet3.createRow(rownum++);
    headerCell = header2.createCell(0);
    headerCell = header2.createCell(1);
    headerCell.setCellValue("Positive");
    headerCell.setCellStyle(headerStyle);
    headerCell = header2.createCell(2);
    headerCell.setCellValue("Negative");
    headerCell.setCellStyle(headerStyle);
    headerCell = header2.createCell(3);
    headerCell.setCellValue("Net");
    headerCell.setCellStyle(headerStyle);
    headerCell = header2.createCell(4);
    headerCell.setCellValue("Positive");
    headerCell.setCellStyle(headerStyle);
    headerCell = header2.createCell(5);
    headerCell.setCellValue("Negative");
    headerCell.setCellStyle(headerStyle);
    headerCell = header2.createCell(6);
    headerCell.setCellValue("Net");
    headerCell.setCellStyle(headerStyle);
    sheet3.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
    sheet3.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheet3.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));

    // Writing Cell Data
    for (i = 0; i < thirdDataSet.length; i++) {
      XSSFRow row = sheet3.createRow(rownum++);
      cellnum = 0;
      for (j = 0; j < thirdDataSet[0].length; j++) {
        XSSFCell cell = row.createCell(cellnum++);
        if (j != 0) {
          cell.setCellValue(Double.parseDouble(thirdDataSet[i][j]));
          cell.setCellStyle(style);
          cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else
          cell.setCellValue(thirdDataSet[i][j]);
      }
    }

    // Creating data sets for chart
    String category1 = "Pos Home";
    String category2 = "Neg Home";
    String category3 = "Net Home";
    String category4 = "Pos Away";
    String category5 = "Neg Away";
    String category6 = "Net Away";
    DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
    for (i = 0; i < thirdDataSet.length; i++) {
      dataset3.setValue(Double.parseDouble(thirdDataSet[i][1]), category1, thirdDataSet[i][0]);
      dataset3.setValue(Double.parseDouble(thirdDataSet[i][2]), category2, thirdDataSet[i][0]);
      dataset3.setValue(Double.parseDouble(thirdDataSet[i][3]), category3, thirdDataSet[i][0]);
      dataset3.setValue(Double.parseDouble(thirdDataSet[i][4]), category4, thirdDataSet[i][0]);
      dataset3.setValue(Double.parseDouble(thirdDataSet[i][5]), category5, thirdDataSet[i][0]);
      dataset3.setValue(Double.parseDouble(thirdDataSet[i][6]), category6, thirdDataSet[i][0]);
    }

    // Creating Chart and writing to excel
    JFreeChart barChart =
        ChartFactory.createBarChart("Home Away Performance", "Team", "Performace", dataset3,
            PlotOrientation.VERTICAL, true, true, false);
    BarRenderer renderer3 = new BarRenderer();
    renderer3.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", format));
    renderer3.setBaseItemLabelsVisible(true);
    renderer3.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
        TextAnchor.BASELINE_CENTER));
    renderer3.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
        TextAnchor.BASELINE_CENTER));
    barChart.getCategoryPlot().setRenderer(renderer3);
    chartOut = new ByteArrayOutputStream();
    ChartUtilities.writeChartAsJPEG(chartOut, barChart, 2000, 500);
    id = workbook.addPicture(chartOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
    chartOut.close();
    drawing = sheet3.createDrawingPatriarch();
    anchor = drawing.createAnchor(0, 0, 0, 0, 8, 0, 15, 10);
    pic = drawing.createPicture(anchor, id);
    pic.resize();

    // Autofitting Column Width
    for (j = 0; j < thirdDataSet[0].length; j++)
      sheet3.autoSizeColumn(j);

    /*
     * Algorithm 4: Half Time Full Time Relation
     */
    XSSFSheet sheet4 = workbook.createSheet("HalfTimeFullTimeRelation");
    String[][] fourthDataSet = a.halfFullRelation();
    rownum = 0;

    // Writing Column headers
    header = sheet4.createRow(rownum++);
    headerCell = header.createCell(0);
    headerCell.setCellValue("Team");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(1);
    headerCell.setCellValue("Home");
    headerCell.setCellStyle(headerStyle);
    headerCell = header.createCell(2);
    headerCell.setCellValue("Away");
    headerCell.setCellStyle(headerStyle);

    // Writing Cell Data
    for (i = 0; i < fourthDataSet.length; i++) {
      XSSFRow row = sheet4.createRow(rownum++);
      cellnum = 0;
      for (j = 0; j < fourthDataSet[0].length; j++) {
        XSSFCell cell = row.createCell(cellnum++);
        if (j != 0) {
          cell.setCellValue(Double.parseDouble(fourthDataSet[i][j]));
          cell.setCellStyle(style);
          cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else
          cell.setCellValue(fourthDataSet[i][j]);
      }
    }

    // Creating data sets for chart
    DefaultCategoryDataset dataset4 = new DefaultCategoryDataset();
    for (i = 0; i < fourthDataSet.length; i++) {
      dataset4.setValue(Double.parseDouble(fourthDataSet[i][1]), "Home", fourthDataSet[i][0]);
      dataset4.setValue(Double.parseDouble(fourthDataSet[i][2]), "Away", fourthDataSet[i][0]);
    }

    // Creating Chart and writing to excel
    JFreeChart stackedChart2 =
        ChartFactory.createStackedBarChart("Half Time Full Time Relation", "Team", "Relation",
            dataset4, PlotOrientation.VERTICAL, true, true, false);
    StackedBarRenderer renderer4 = new StackedBarRenderer(false);
    renderer4.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    renderer4.setBaseItemLabelsVisible(true);
    renderer4.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
        TextAnchor.BASELINE_CENTER));
    renderer4.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
        TextAnchor.BASELINE_CENTER));
    stackedChart2.getCategoryPlot().setRenderer(renderer4);
    chartOut = new ByteArrayOutputStream();
    ChartUtilities.writeChartAsJPEG(chartOut, stackedChart2, 2000, 500);
    id = workbook.addPicture(chartOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
    chartOut.close();
    drawing = sheet4.createDrawingPatriarch();
    anchor = drawing.createAnchor(0, 0, 0, 0, 4, 0, 15, 10);
    pic = drawing.createPicture(anchor, id);
    pic.resize();

    // Autofitting Column Width
    for (j = 0; j < fourthDataSet[0].length; j++)
      sheet4.autoSizeColumn(j);


    // Outputting everything to the file
    try {
      FileOutputStream out = new FileOutputStream(new File(outputFolder));
      workbook.write(out);
      out.close();
      System.out.println("Excel File with results and Visualization written successfully");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}