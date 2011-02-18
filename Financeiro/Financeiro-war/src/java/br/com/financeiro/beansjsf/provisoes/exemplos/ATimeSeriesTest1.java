/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.beansjsf.provisoes.exemplos;

/* --------------------
* TimeSeriesDemo1.java
* --------------------
* (C) Copyright 2002-2005, by Object Refinery Limited.
*
*/

//package demo;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.RectangleEdge;
import java.awt.geom.Rectangle2D;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;

import java.util.Date;
import java.text.DateFormat;


/**
* An example of a time series chart.  For the most part, default settings are
* used, except that the renderer is modified to show filled shapes (as well as
* lines) at each data point.
* <p>
* IMPORTANT NOTE:  THIS DEMO IS DOCUMENTED IN THE JFREECHART DEVELOPER GUIDE.
* DO NOT MAKE CHANGES WITHOUT UPDATING THE GUIDE ALSO!!
*/
public class ATimeSeriesTest1 extends ApplicationFrame implements ChartMouseListener {

   JFreeChart chart;
   ChartPanel chartPanel;

    /**
     * A demonstration application showing how to create a simple time series
     * chart.  This example uses monthly data.
     *
     * @param title  the frame title.
     */
    public ATimeSeriesTest1(String title) {
        super(title);
        XYDataset dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 400));
        chartPanel.setMouseZoomable(true, false);
        this.chartPanel.addChartMouseListener(this);
        setContentPane(chartPanel);
    }

    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Legal & General Unit Trust Prices",  // title
            "Date",             // x-axis label
            "Price Per Unit",   // y-axis label
            dataset,            // data
            true,               // create legend?
            true,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

        plot.setDomainCrosshairVisible(true);
      plot.setRangeCrosshairVisible(true);


        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;

    }
    /**
     * Receives chart mouse click events.
     *
     * @param event  the event.
     */
    public void chartMouseClicked(ChartMouseEvent event) {
        int mouseX = event.getTrigger().getX();
        int mouseY = event.getTrigger().getY();

        System.out.println("SCREEN x = " + mouseX + ", y = " + mouseY);

        Point2D p = this.chartPanel.translateScreenToJava2D(
                new Point(mouseX, mouseY));

        XYPlot plot = (XYPlot) this.chart.getPlot();
        ChartRenderingInfo info = this.chartPanel.getChartRenderingInfo();
        Rectangle2D dataArea = info.getPlotInfo().getDataArea();

        DateAxis domainAxis = (DateAxis)plot.getDomainAxis();
        RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();

        ValueAxis rangeAxis = plot.getRangeAxis();
        RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

        double chartX = domainAxis.java2DToValue(p.getX(), dataArea,
                domainAxisEdge);
        double chartY = rangeAxis.java2DToValue(p.getY(), dataArea,
                rangeAxisEdge);
        System.out.println("Chart: x = " + chartX + ", y = " + chartY);

        double xx = plot.getDomainCrosshairValue();
        System.out.println("CROSS HAIR VALUE " + xx);

      long millis = (long) chartX;
      Date date = new Date(millis);
      DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
      String formattedDate = df.format(date);
      System.out.println("The  click point Date is " + formattedDate+ "  millis is "+ millis);

///this does not return correct values, crosshair xx value is incorrect

        long millis2 = (long) xx;
      Date date2 = new Date(millis2);

      formattedDate = df.format(date2);
      System.out.println("The crosshair Date is " + formattedDate+ "  millis is "+ millis2);

      TimeSeriesCollection timeSeriesCollection = (TimeSeriesCollection) plot.getDataset(0);

      TimeSeries   timeSeries = timeSeriesCollection.getSeries(0);
      Date timeDate = new Date(millis);

      Day timeDay = new Day (timeDate);

      int itemIndex = timeSeries.getIndex(  timeDay );




      System.out.println("The itemIndex is " + itemIndex);

      TimeSeriesDataItem timeSeriesDataItem  = timeSeries.getDataItem(itemIndex  );
      double value = timeSeriesDataItem.getValue().doubleValue() ;
      System.out.println("The value  is " + value );

      System.out.println("These last values NEVER seem to change no matter where I click.  Help" );


    }

    /**
     * Receives chart mouse moved events.
     *
     * @param event  the event.
     */
    public void chartMouseMoved(ChartMouseEvent event) {
        // ignore
    }



    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return the dataset.
     */
    private static XYDataset createDataset() {

        TimeSeries s1 = new TimeSeries("L&G European Index Trust", Month.class);
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
   

        TimeSeries s2 = new TimeSeries("L&G UK Index Trust", Month.class);
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        ATimeSeriesTest1 demo = new ATimeSeriesTest1("ATimeSeriesTest1");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}