/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.charts;

import br.com.financeiro.auxjsf.charts.store.ChartStore;
import br.com.financeiro.auxjsf.jfreechart.ParetoJfreeChart;
import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;
import br.com.financeiro.entidades.User;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
/**
 *
 * @author gbvbahia
 */
public class CartaoCreditoChart extends ChartStore{

    public CartaoCreditoChart(FacesContext fc, User proprietario, Locale locale) {
        super(fc, proprietario, locale);
    }

    @Override
    public String getCaminhoChar(Object[] objetosGrad) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Double> mapPareto = (Map<String, Double>)objetosGrad[0];
        Date mesCartao = (Date) objetosGrad[1];
        ParetoJfreeChart pareto = new ParetoJfreeChart();
        SimpleDateFormat sd = new SimpleDateFormat("MMMM", super.getLocale());
        JFreeChart grafico = pareto.getParetoChart(
                UtilMetodos.getResourceBundle("tituloParetoCC", fc).replace("*", "(" + sd.format(mesCartao) +")"),
                UtilMetodos.getResourceBundle("paretoCCLegendaHorizontal", fc),
                UtilMetodos.getResourceBundle("paretoCCLegendaVertical", fc),
                UtilMetodos.getResourceBundle("paretoCClegendaHorizontalPercentual", fc),
                UtilMetodos.getResourceBundle("paretoCClegendaVerticalPercentual", fc),
                mapPareto, null);
        //mapPareto = new LinkedHashMap<String, Double>();
        ((JFreeChart) grafico).getTitle().setFont(new Font("Arial", Font.BOLD, 17));
        ((JFreeChart) grafico).getTitle().setPaint(new java.awt.Color(102, 124, 75));//#999999
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
