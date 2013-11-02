/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package footballStats;

/**
 *
 * @author NEEL
 */
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
//package de.vogella.jfreechart.swing.pie;


public class PieChart extends JFrame {

  private static final long serialVersionUID = 1L;

  public PieChart(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        // This will create the dataset
        PieDataset dataset = createDataset();
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, chartTitle);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);

    }


/**
     * Creates a sample dataset
     */

    private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();









        result.setValue("B365",1.972408942883281 );
        result.setValue("BW",1.8532007470631393 );
        result.setValue("GB",1.8471793376072962 );
        result.setValue("IW",1.7221150709235857 );
        result.setValue("LB",1.7781106646625173);
        result.setValue("PS",2.032399731965696 );
        result.setValue("WH",1.809443590917902);
        result.setValue("SJ",1.9562401438109553);
        result.setValue("VC",1.9493107281295567 );
        result.setValue("BS",1.814855208190922 );

        
        return result;

    }


/**
     * Creates a chart
     */

    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset,true,true,false);//ChartFactory.createPieChart3D(title,          // chart titledataset,                // data
         

       // Plot plot = (Plot)chart.getPlot();
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;

    }
}