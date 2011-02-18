/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.principal;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.cadastro.contas.PagamentoContaFaces;
import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;
import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.ejbbeans.interfaces.MovimentacaoFinanceiraLocal;
import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.MovimentacaoFinanceira;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
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
public class ContasEmAbertoFaces implements Observer {

    @EJB
    private ContaBancariaLocal contaBancariaBean;
    @EJB
    private MovimentacaoFinanceiraLocal movimentacaoFinanceiraBean;
    @EJB
    private RelatoriosLocal relatoriosBean;
    private User proprietario;
    private DataModel dataModel;
    private Locale locale = new Locale("pt", "BR");
    private Date initBusca;
    private int limiteContas = 10;

    /** Creates a new instance of ContasEmAberto */
    public ContasEmAbertoFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        this.initBusca = c.getTime();
    }

    public String getGraficoPizzaNPagoPago() throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("MMM'/'yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(initBusca);
        Color cor = new Color(237, 234, 221);
        Double[] d = this.relatoriosBean.buscarNPagoPago(proprietario, c);
        DefaultPieDataset dataset = new DefaultPieDataset();
        String apagar = UtilMetodos.getResourceBundle("contasNPagas", FacesContext.getCurrentInstance()) + " " + UtilMetodos.currencyFormat(d[0]);
        String pagas = UtilMetodos.getResourceBundle("contasPagas", FacesContext.getCurrentInstance())+ " " + UtilMetodos.currencyFormat(d[1]);
        dataset.setValue(apagar, d[0]);
        dataset.setValue(pagas, d[1]);
        JFreeChart grafico = ChartFactory.createPieChart(sd.format(initBusca),
                dataset, true, false, this.locale);
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

    public void atualizaPagamento(ActionEvent event) {
        Conta conta = (Conta) this.dataModel.getRowData();
        if (conta.getContaMovimentacaoFinanceira().getContaBancaria() == null) {
            UtilMetodos.messageFactoringFull("contaMovimentacaoNaoInformada", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            return;
        }
        try {
            this.movimentacaoFinanceiraBean.criarMovimentacaoFinanceira(conta);
            ControleObserver.notificaObservers(proprietario);
            UtilMetodos.messageFactoringFull("movimentacaoCriada", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } catch (MovimentacaoFinanceiraException me) {
            FacesMessage msg = new FacesMessage(me.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "MovimentacaoFinanceiraException");
        }
    }

    public List<SelectItem> getContaBancariasItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (ContaBancaria conta : this.contaBancariaBean.contasProprietario(proprietario)) {
            toReturn.add(new SelectItem(conta, conta.getLabel()));
        }
        return toReturn;
    }

    public void atualizaModelContas(ActionEvent event) {
        List<Conta> contasNaoVencidas = this.movimentacaoFinanceiraBean.buscarContasNaoVencidas(proprietario, initBusca, limiteContas);
        for (Conta conta : contasNaoVencidas) {
            conta.setContaMovimentacaoFinanceira(new MovimentacaoFinanceira());
        }
        this.dataModel = new ListDataModel(contasNaoVencidas);
    }

    public DataModel getDataModel() {
        if (this.dataModel == null) {
            atualizaModelContas(null);
        }
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Date getInitBusca() {
        return initBusca;
    }

    public void setInitBusca(Date initBusca) {
        this.initBusca = initBusca;
    }

    public int getLimiteContas() {
        return limiteContas;
    }

    public void setLimiteContas(int limiteContas) {
        this.limiteContas = limiteContas;
    }

    public void update(Observable o, Object arg) {
        atualizaModelContas(null);
    }
}
