/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.jfreechart;

import java.text.NumberFormat;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.util.SortOrder;

/**
 *
 * @author gbvbahia
 */
public class ParetoJfreeChart {

/**
 * Parametro subTitulos não obrigatório, caso não tenha passe nulo
 * O Map leendasValores deve representar a legenda de cada barra do gráfico com seus respectivos valores
 * @param tituloPrincipal
 * @param legendaHorizontal
 * @param legendaVertical
 * @param legendaHorizontalPercentual
 * @param legendaVerticalPercentual
 * @param legendasValores
 * @param subTitulos
 * @return
 */
    public JFreeChart getParetoChart(String tituloPrincipal, String legendaHorizontal, String legendaVertical, String legendaHorizontalPercentual,String legendaVerticalPercentual, Map<String,Double> legendasValores, String[] subTitulos){
        CategoryDataset[] datasets = createDatasets(legendasValores, legendaHorizontal, legendaHorizontalPercentual);
        JFreeChart chart = ChartFactory.createBarChart(
            tituloPrincipal,  // TITULO PRINCIPAL DO GRÁFICO
            "",                     // RÓTULO DAS BARRAS
            legendaVertical,                     // RÓTULO VERTICAL DAS BARRAS (ESQUERDO)
            datasets[0],                        // data
            PlotOrientation.VERTICAL,
            true,                           // INCLUIR LEGENDA
            true,
            false
        );
        if(subTitulos != null && subTitulos.length > 0){
            for(String subT : subTitulos){
                chart.addSubtitle(new TextTitle(subT));
            }
        }

            // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

        NumberAxis axis2 = new NumberAxis(legendaVerticalPercentual);
        axis2.setNumberFormatOverride(NumberFormat.getPercentInstance());
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, datasets[1]);
        plot.setRenderer(1, renderer2);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        ChartUtilities.applyCurrentTheme(chart);
        return chart;
    }

    private CategoryDataset[] createDatasets(Map<String, Double> legendasValores, String legendaHorizontal, String legendaHorizontalPercentual) {
        DefaultKeyedValues data = new DefaultKeyedValues();
        Set<String> mapSet = legendasValores.keySet();
        for(String valor:mapSet){
            data.addValue(valor, legendasValores.get(valor));
        }
        data.sortByValues(SortOrder.DESCENDING);
        KeyedValues cumulative = DataUtilities.getCumulativePercentages(data);
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
                legendaHorizontal, data);
        CategoryDataset dataset2 = DatasetUtilities.createCategoryDataset(
                legendaHorizontalPercentual, cumulative);
        return new CategoryDataset[] { dataset, dataset2 };
    }
}
