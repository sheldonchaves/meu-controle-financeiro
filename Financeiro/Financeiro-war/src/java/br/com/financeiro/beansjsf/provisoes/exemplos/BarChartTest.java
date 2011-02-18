/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.beansjsf.provisoes.exemplos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class BarChartTest extends ApplicationFrame {

   public final String DATEFORMAT_MONTH = "MMMM";
   public static final Paint LIGHT_RED = new java.awt.Color(255,191,233);
   public static final Paint COBALD = new java.awt.Color(40,71,102);
   public static final Paint WHITE = new java.awt.Color(255, 255, 255);
   Date startDate = formatDate("01.01.2007 00:00:00"); //Begin of time range for x-axis
   Date endDate = formatDate("30.05.2007 00:00:00");  //End of time range for x-axis
   String labelXAxis1 = "Month";
   String labelYAxis1 = "Caller";
   String title = "Test BarGraph";
   int intervalInMinutes = 86400;
   TimeSeries tSeries1 = new TimeSeries("Total Calls", Month.class); // time series for graph, monthly view
   TimeSeries tSeries2 = new TimeSeries("Successfull Calls", Month.class); //
   JFreeChart chart;
   XYPlot plot;
   NumberAxis axisNum;
   DateAxis axis;

   /**
    * A test application showing a horizontal bar  chart.
    */
   private static final long serialVersionUID = 1L;

      /**
        * Creates a new demo instance.
        *
        * @param title  the frame title.
        */
       public BarChartTest(String title) {
          super(title);
          initTimeSeries();
          generateChart();
           ChartPanel chartPanel = new ChartPanel(chart);
           chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
           setContentPane(chartPanel);
       }

       /**
        * Initalize time series with a sample dataset.
        *
        */
       private void initTimeSeries() {
          List dates = new ArrayList();
          dates.add(formatDate("01.01.2007 00:00:00"));
          dates.add(formatDate("01.02.2007 00:00:00"));
          dates.add(formatDate("01.03.2007 00:00:00"));
          dates.add(formatDate("01.04.2007 00:00:00"));
          dates.add(formatDate("01.05.2007 00:00:00"));
       Iterator it = dates.iterator();
          while ( it.hasNext() ) {
             Date myDate = (Date) it.next();
             tSeries1.add(new Month ((Date) myDate), 25); //
             tSeries2.add(new Month ((Date) myDate), 10); //
          }
       }

       /**
        *  generate JFreeGraph chart
        */
       private void generateChart() {
         //Create dataset needed by chart with time series
         TimeSeriesCollection dataset = new TimeSeriesCollection();
         dataset.addSeries(tSeries1);
         dataset.addSeries(tSeries2);

         chart = ChartFactory.createXYBarChart(
               this.getTitle(), //title
               "Month", // x-axis label
               true, // date axis?
               "Total Calls", // y-axis label
               dataset, // data
               PlotOrientation.VERTICAL, // orientation
               true, // create legend?
               true, // generate tooltips?
               false // generate URLs?
               );

         // customise chart
         chart.setBackgroundPaint(WHITE);

         //initialize the plot
         this.plot = chart.getXYPlot();

         //set the range axis to display integers only...
         ClusteredXYBarRenderer r = new ClusteredXYBarRenderer();

         r.setItemLabelGenerator(
                  new StandardXYItemLabelGenerator()
              );

            r.setSeriesPaint(0, LIGHT_RED);
         r.setSeriesPaint(1, COBALD);

         // the margin between two bars is specified as a percentage of the bar
         // width
         // (for example, 0.10 is ten percent) and is the amount
         // that is trimmed from the bar width before the bar is displayed.
         r.setMargin(0.1);

         r.setToolTipGenerator(
                  new StandardXYToolTipGenerator(
                      StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                      new SimpleDateFormat("d.MM.y HH:mm"), new DecimalFormat("0,000.0")
                  )
              );
         plot.setRenderer(r);
         //set colors
         plot.setBackgroundPaint(Color.lightGray);
         plot.setRangeGridlinePaint(Color.white);

         //Init Range (Y-Axis)
         this.axisNum = (NumberAxis) plot.getRangeAxis();
         //Use only integers for y-axis
         TickUnitSource units = NumberAxis.createIntegerTickUnits();
         axisNum.setStandardTickUnits(units);
         axisNum.setLabelFont(new Font("Arial", Font.PLAIN, 10));

         //Init Domain (X-Axis)
         //User date for x-axis
         this.axis = (DateAxis) plot.getDomainAxis();
         TickUnitSource unitsAxis = DateAxis.createStandardDateTickUnits();
         this.axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
         this.axis.setStandardTickUnits(unitsAxis);
         this.axis.setRange(startDate, endDate);
         this.axis.setMinimumDate(startDate);
         this.axis.setMaximumDate(endDate);

         axis.setDateFormatOverride(new SimpleDateFormat(DATEFORMAT_MONTH, new Locale("en")));
         chart.setBackgroundPaint(WHITE);

         ((JFreeChart) chart).getTitle().setFont(new Font("Arial", Font.TRUETYPE_FONT, 17));
         ((JFreeChart) chart).getTitle().setPaint(new java.awt.Color(153, 153, 153));//#999999

      }


       /**
        * Format a string to a date object
        * @param myDate
        * @return
        */
      private Date formatDate(String myDate) {

         DateFormat format = DateFormat.getDateTimeInstance();
         Date d = null;
         try {
            d = format.parse(myDate);
         } catch ( ParseException  e1 ) {}

         return d;
      }

       /**
        * Starting point for the demonstration application.
        *
        * @param args  ignored.
        */
       public static void main(String[] args) {

           BarChartTest demo = new BarChartTest("Bar Chart Test Time Series");
           demo.pack();
           RefineryUtilities.centerFrameOnScreen(demo);
           demo.setVisible(true);

       }

   }


