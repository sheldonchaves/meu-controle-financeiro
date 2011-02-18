/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.provisoes;

import br.com.financeiro.auxjsf.classes.AcumuladoHelper;
import br.com.financeiro.auxjsf.classes.LinhasReportHelper;
import br.com.financeiro.auxjsf.classes.TipoValor;
import br.com.financeiro.auxjsf.classes.comparators.AcumuladoComparator;
import br.com.financeiro.auxjsf.classes.interfaces.AcumuladoInterface;
import br.com.financeiro.auxjsf.classes.interfaces.TipoValorInterface;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
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
public class AcumuladoFaces {

    @EJB
    private RelatoriosLocal relatoriosBean;
    private Locale locale = new Locale("pt", "BR");
    private User proprietario;
    private Date dataReferencia = Calendar.getInstance().getTime();
    private int meses = 2;
    private DataModel acumuladoHelperDataModel;
    private LinhasReportHelper linhasReportHelper;

    private void clean() {
        this.acumuladoHelperDataModel = null;
        linhasReportHelper = null;
    }

    /** Creates a new instance of AcumuladoFaces */
    public AcumuladoFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        //Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.INFO, "Um bean AcumuladoFaces acaba de ser criado!");
    }

    //Getters and setters
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getMeses() {
        return meses;
    }

    public void setMeses(int meses) {
        this.meses = meses;
    }

    public DataModel getAcumuladoHelperDataModel() {
        if (this.acumuladoHelperDataModel == null) {
            atualizaMeses(null);
        }
        return this.acumuladoHelperDataModel;
    }

    public void setAcumuladoHelperDataModel(DataModel acumuladoHelperDataModel) {
        this.acumuladoHelperDataModel = acumuladoHelperDataModel;
    }

    public void atualizaMeses(ActionEvent event) {
        clean();
        geraAcumuladoDataModel();
    }

    private AcumuladoInterface geraAcumulado(int loopI) {
        String receita = UtilMetodos.getResourceBundle("receita", FacesContext.getCurrentInstance());
        String despesa = UtilMetodos.getResourceBundle("despesa", FacesContext.getCurrentInstance());
        AcumuladoHelper acumulado = new AcumuladoHelper();
        Calendar c = Calendar.getInstance();
        c.setTime(dataReferencia);
        c.add(Calendar.MONTH, loopI);
        acumulado.setMesAno(c.getTime());
        acumulado.add(new TipoValor(receita, this.relatoriosBean.receitaAcumuladaMes(c, proprietario)));
        acumulado.add(new TipoValor(despesa, this.relatoriosBean.pagamentoAcumuladoMes(c, proprietario)));
        if (loopI == 0) {
            acumulado.setMesAtual(true);
        }
        return acumulado;
    }

    private DataModel geraAcumuladoDataModel() {
        List<AcumuladoInterface> toDataModel = new ArrayList<AcumuladoInterface>();
        for (int i = 0; i <= meses; i++) {
            toDataModel.add(geraAcumulado(i));
            if (i != 0) {
                toDataModel.add(geraAcumulado(i * (-1)));
            }
        }
        Collections.sort(toDataModel, new AcumuladoComparator());
        this.acumuladoHelperDataModel = new ListDataModel(toDataModel);
        List<TipoValorInterface> listReceita = new ArrayList<TipoValorInterface>();
        List<TipoValorInterface> listDespesa = new ArrayList<TipoValorInterface>();
        for (AcumuladoInterface aci : toDataModel) {
            for (TipoValorInterface tv : aci.getTipoValorInterface()) {
                if (tv.getTipo().equals(UtilMetodos.getResourceBundle("receita", FacesContext.getCurrentInstance()))) {
                    listReceita.add(tv);
                } else if (tv.getTipo().equals(UtilMetodos.getResourceBundle("despesa", FacesContext.getCurrentInstance()))) {
                    listDespesa.add(tv);
                }
            }
        }
        getLinhasReportHelper().addTipoValor(listDespesa);
        getLinhasReportHelper().addTipoValor(listReceita);
        return this.acumuladoHelperDataModel;
    }



    public String getGraficoAcumulado() throws ParseException {
        List<AcumuladoInterface> acumulados = (List<AcumuladoInterface>) getAcumuladoHelperDataModel().getWrappedData();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sd = new SimpleDateFormat("MMM'/'yyyy", this.locale);
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
        deletaFiles(this.proprietario.getId() + "_" + fileName + ".png");
        return "/temp/" + this.proprietario.getId() + "_" + fileName + ".png";
    }

    private void deletaFiles(String excecaoNome) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        File file = new File(session.getServletContext().getRealPath("temp"));
        File[] files = file.listFiles();
        for (File f : files) {
            Calendar c = Calendar.getInstance();
            Calendar ff = Calendar.getInstance();
            ff.setTime(new Date(f.lastModified()));
            int diaH = c.get(Calendar.DAY_OF_MONTH);
            int diaF = ff.get(Calendar.DAY_OF_MONTH);
            if (StringUtils.substringBefore(f.getName(), "_").equals(StringUtils.substringBefore(excecaoNome, "_"))
                    && !StringUtils.substringAfter(f.getName(), "_").equals(StringUtils.substringAfter(excecaoNome, "_"))
                    && diaH != diaF) {
                f.delete();
            }
        }
    }

    private String getCaminhoLogo(String nome) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String caminho = session.getServletContext().getRealPath("temp");
        File temp = new File(caminho);
        temp.mkdir();
        //Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.INFO, caminho + File.separator + this.proprietario.getId() + ".png");
        return caminho + File.separator + this.proprietario.getId() + "_" + nome + ".png";
    }

    public LinhasReportHelper getLinhasReportHelper() {
        if (this.linhasReportHelper == null) {
            this.linhasReportHelper = new LinhasReportHelper();
        }
        return linhasReportHelper;
    }

    public void setLinhasReportHelper(LinhasReportHelper linhasReportHelper) {
        this.linhasReportHelper = linhasReportHelper;
    }
}
