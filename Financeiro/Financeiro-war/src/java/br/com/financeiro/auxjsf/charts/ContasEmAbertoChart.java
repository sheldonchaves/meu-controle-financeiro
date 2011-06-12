/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.charts;

import br.com.financeiro.auxjsf.charts.factoring.ChartStore;

import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;
import br.com.financeiro.entidades.User;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author gbvbahia
 */
public class ContasEmAbertoChart extends ChartStore {

    public ContasEmAbertoChart(FacesContext fc, User proprietario, Locale locale) {
        super(fc, proprietario, locale);
    }

    @Override
    public String getCaminhoChar(Object[] objetosGrad) {
        Date initBusca = (Date) objetosGrad[0];
        Double[] d = (Double[]) objetosGrad[1];
        SimpleDateFormat sd = new SimpleDateFormat("MMM'/'yyyy");
        Color cor = new Color(237, 234, 221);
        DefaultPieDataset dataset = new DefaultPieDataset();
        String apagar = UtilMetodos.getResourceBundle("contasNPagas", FacesContext.getCurrentInstance()) + " " + UtilMetodos.currencyFormat(d[0]);
        String pagas = UtilMetodos.getResourceBundle("contasPagas", FacesContext.getCurrentInstance()) + " " + UtilMetodos.currencyFormat(d[1]);
        dataset.setValue(apagar, d[0]);
        dataset.setValue(pagas, d[1]);
        JFreeChart grafico = ChartFactory.createPieChart(sd.format(initBusca),
                dataset, true, false, super.getLocale());
        grafico.setBackgroundPaint(cor);
        grafico.setBorderPaint(cor);
        PiePlot pieplot = (PiePlot) grafico.getPlot();
        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));
        pieplot.setStartAngle(0);
        pieplot.setExplodePercent(apagar, 0.2);
        pieplot.setSectionOutlinePaint(apagar, Color.RED);
        pieplot.setSectionPaint(apagar, Color.RED);
        pieplot.setSectionOutlinePaint(pagas, Color.GREEN);
        pieplot.setSectionPaint(pagas, Color.GREEN);
        pieplot.setBackgroundPaint(cor);
        pieplot.setBaseSectionOutlinePaint(cor);
        pieplot.setBaseSectionPaint(cor);
        pieplot.setOutlinePaint(cor);
        String fileName = System.currentTimeMillis() + "";
        File file = new File(getCaminhoLogo(fileName));
        try {
            ChartUtilities.saveChartAsPNG(file, grafico, 250, 200);
        } catch (IOException ex) {
            Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.SEVERE, "Problema na rederização do gráfico!", ex);
        }
        deletaFiles(super.getProprietario().getId() + "_" + fileName + ".png");
        return "/temp/" + super.getProprietario().getId() + "_" + fileName + ".png";
    }
}
