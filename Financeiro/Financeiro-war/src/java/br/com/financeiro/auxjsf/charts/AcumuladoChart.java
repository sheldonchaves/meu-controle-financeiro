/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.charts;

import br.com.financeiro.auxjsf.charts.store.ChartStore;
import br.com.financeiro.auxjsf.classes.interfaces.AcumuladoInterface;
import br.com.financeiro.auxjsf.classes.interfaces.TipoValorInterface;
import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;
import br.com.financeiro.entidades.User;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
/**
 *
 * @author gbvbahia
 */
public class AcumuladoChart extends ChartStore{

    public AcumuladoChart(FacesContext fc, User proprietario, Locale locale) {
        super(fc, proprietario, locale);
    }

    @Override
    public String getCaminhoChar(Object[] objetosGrad) {
         List<AcumuladoInterface> acumulados = (List<AcumuladoInterface>) objetosGrad[0];
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sd = new SimpleDateFormat("MMM'/'yyyy", super.getLocale());
        for (AcumuladoInterface ai : acumulados) {
            for (TipoValorInterface tv : ai.getTipoValorInterface()) {
                dataset.addValue(tv.getValor(), tv.getTipo(), sd.format(ai.getMesAno()));
            }
        }
        JFreeChart grafico = ChartFactory.createBarChart(
                UtilMetodos.getResourceBundle("realizadoXprovisionado", FacesContext.getCurrentInstance()),
                UtilMetodos.getResourceBundle("periodo", FacesContext.getCurrentInstance()),
                UtilMetodos.getResourceBundle("emReais", FacesContext.getCurrentInstance()),
                dataset, PlotOrientation.VERTICAL, true, false, false);
        CategoryPlot plot = grafico.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.BLUE);
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.RED);
        ((JFreeChart) grafico).getTitle().setFont(new Font("Arial", Font.BOLD, 17));
        ((JFreeChart) grafico).getTitle().setPaint(new java.awt.Color(102, 124, 75));
        String fileName = System.currentTimeMillis() + "";
        File file = new File(getCaminhoLogo(fileName));
        try {
            ChartUtilities.saveChartAsPNG(file, grafico, 1050, 320);
        } catch (IOException ex) {
            Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.SEVERE, "Problema na rederização do gráfico!", ex);
        }
        deletaFiles(super.getProprietario().getId() + "_" + fileName + ".png");
        return "/temp/" + super.getProprietario().getId() + "_" + fileName + ".png";
    }
    
    
}
