/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.charts;

import br.com.financeiro.auxjsf.charts.factoring.ChartStore;

import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;

import br.com.financeiro.auxjsf.classes.TipoValores;
import br.com.financeiro.auxjsf.classes.comparators.TipoValoresComparator;
import br.com.financeiro.entidades.User;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author gbvbahia
 */
public class GrupoGastoPagamentosChart extends ChartStore {

    public GrupoGastoPagamentosChart(FacesContext fc, User proprietario, Locale locale) {
        super(fc, proprietario, locale);
    }

    @Override
    public String getCaminhoChar(Object[] objetosGrad) {
        XYDataset dataset = createDataset((List<TipoValores>) objetosGrad[0]);
        JFreeChart chart = createChart(dataset);
        String fileName = System.currentTimeMillis() + "";
        File file = new File(getCaminhoLogo(fileName));
        try {
            ChartUtilities.saveChartAsPNG(file, chart, 1050, 320);
        } catch (IOException ex) {
            Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.SEVERE, "Problema na rederização do gráfico!", ex);
        }
        deletaFiles(super.getProprietario().getId() + "_" + fileName + ".png");
        return "/temp/" + super.getProprietario().getId() + "_" + fileName + ".png";
    }

    private XYDataset createDataset(List<TipoValores> listTipoValores) {
        Collections.sort(listTipoValores, new TipoValoresComparator());
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        int x = 0;
        for (TipoValores tvs : listTipoValores) {
            x++;
            final TimeSeries series = new TimeSeries(tvs.getTipo());
            for (int i = 0; i < tvs.getValores().size(); i++) {
                try {
                    series.addOrUpdate(new Month(tvs.getMeses().get(i) + 1, tvs.getAnos().get(i)), tvs.getValores().get(i));
                } catch (SeriesException e) {
                    System.out.println("Mes:" + (tvs.getMeses().get(i) + 1) + " Ano:" + tvs.getAnos().get(i) + "Valor" + tvs.getValores().get(i));
                    throw e;
                }
            }
            dataset.addSeries(series);
            if (x == 6) {
                break;
            }
        }
        return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                UtilMetodos.getResourceBundle("grupoGasto2", FacesContext.getCurrentInstance()), // chart title
                UtilMetodos.getResourceBundle("periodo", FacesContext.getCurrentInstance()),
                UtilMetodos.getResourceBundle("emReais", FacesContext.getCurrentInstance()),// y axis label
                dataset, // data
                true, // include legend
                true, // tooltips
                false // urls
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
            renderer.setDrawSeriesLineAsPath(true);
        }
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        TickUnitSource unitsAxis = DateAxis.createStandardDateTickUnits();
        axis.setTickMarkPosition(DateTickMarkPosition.START);
        axis.setStandardTickUnits(unitsAxis);
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy", super.getLocale()));
        axis.configure();
        ((JFreeChart) chart).getTitle().setFont(new Font("Arial", Font.BOLD, 17));
        ((JFreeChart) chart).getTitle().setPaint(new java.awt.Color(102, 124, 75));//#999999
        return chart;
    }
}
