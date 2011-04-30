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
import br.com.financeiro.auxjsf.jfreechart.ParetoJfreeChart;
import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.FormaPagamento;
import br.com.financeiro.entidades.enums.StatusPagamento;
import br.com.financeiro.utils.UtilMetodos;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author gbvbahia
 */
public class CartaoCreditoFaces implements Observer{
    @EJB
    private CartaoCreditoLocal cartaoCreditoBean;

    @EJB
    private RelatoriosLocal relatoriosBean;
    private Locale locale = new Locale("pt", "BR");
    private User proprietario;
    private int meses = 2;
    private Date dataReferencia = Calendar.getInstance().getTime();
    private DataModel cartaoDataModel;
    private LinhasReportHelper linhasReportHelper;
    private CartaoCreditoUnico cartaoCreditoUnico;
    private Date mesCartao;
    private double totalPagar;
    private double totalFatura;
    private DataModel dataModelContas;
    private Map<String, Double> mapPareto;
    {
        mapPareto = new LinkedHashMap<String, Double>();
        Calendar c = Calendar.getInstance();
        mesCartao = c.getTime();
    }

    /** Creates a new instance of CartaoCreditoFaces */
    public CartaoCreditoFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
        //Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.INFO, "Um bean CartaoCreditoFaces acaba de ser criado!");
    }

    public void atualizaValoresCartao(ActionEvent event) {
        geraCartoes();
        this.dataModelContas = null;
    }

     public void atualizarListaContasCartaoCredito(ActionEvent event) {
        List<ContaPagar> toReturn = this.cartaoCreditoBean.buscarContasPorCartaoCredito(cartaoCreditoUnico, mesCartao);
        this.totalPagar = 0.0;
        this.totalFatura = 0.0;
        for (ContaPagar cp : toReturn) {
            if (cp.getStatusPagamento().equals(StatusPagamento.NAO_PAGA)) {
                totalPagar += cp.getValor();
            }
            totalFatura += cp.getValor();
        }
        this.carregaMapPareto(toReturn);
        this.dataModelContas = new ListDataModel(toReturn);
    }

    public List<SelectItem> getCartoesCredito() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        toReturn.add(new SelectItem("Selecione", "Selecione"));
        for (CartaoCreditoUnico cc : this.cartaoCreditoBean.buscaCartaoPorStatus(true, proprietario)) {
            toReturn.add(new SelectItem(cc, cc.getLabelCartao()));
        }
        return toReturn;
    }

    private DataModel geraCartoes() {
        List<AcumuladoInterface> toDataModel = new ArrayList<AcumuladoInterface>();
        for (int i = 0; i <= meses; i++) {
            toDataModel.add(geraCartao(i));
            if (i != 0) {
                toDataModel.add(geraCartao(i * (-1)));
            }
        }
        Collections.sort(toDataModel, new AcumuladoComparator());
        List<List<TipoValorInterface>> listAll = new ArrayList<List<TipoValorInterface>>();
        
        //VERIFICA QUAL A QUANTIDADE MAIOR DE CARTÕES EM UM MES E GARANTE A ORDEM INSERINDO CARTÕES VAZIOS
        //ONDE A QUANTIDADE É MENOR
        int mes = 0;
        int totalMes = 0;
        for (AcumuladoInterface acu : toDataModel) {
            if(totalMes < acu.getTipoValorInterface().size())
                totalMes = acu.getTipoValorInterface().size();
        }
        for (AcumuladoInterface acu : toDataModel) {
            for (int g = 0; g < totalMes; g++) {
                if ((listAll.size() - 1) >= mes) {
                    if (acu.getTipoValorInterface().size() - 1 >= g) {
                        listAll.get(mes).add(acu.getTipoValorInterface().get(g));
                    } else {
                        listAll.get(mes).add(new TipoValor("", null));
                    }
                } else {
                    listAll.add(new ArrayList<TipoValorInterface>());
                    if (acu.getTipoValorInterface().size() - 1 >= g) {
                        listAll.get(mes).add(acu.getTipoValorInterface().get(g));
                    } else {
                        listAll.get(mes).add(new TipoValor("", null));
                    }
                }
                ++mes;
            }
            mes = 0;
        }

        this.getLinhasReportHelper().setTipoValores(listAll);
        this.cartaoDataModel = new ListDataModel(toDataModel);
        return this.cartaoDataModel;
    }

    private AcumuladoInterface geraCartao(int loop) {
        AcumuladoHelper acumulado = new AcumuladoHelper();
        Calendar c = Calendar.getInstance();
        c.setTime(dataReferencia);
        c.add(Calendar.MONTH, loop);
        acumulado.setMesAno(c.getTime());
        List<Object[]> cartaoValores = this.relatoriosBean.contaPagarFormaPagto(FormaPagamento.CARTAO_DE_CREDITO, proprietario, c);
        if (cartaoValores.isEmpty()) {
            TipoValor tv = new TipoValor(UtilMetodos.getResourceBundle("semInformacao", FacesContext.getCurrentInstance()), 0.00);
            acumulado.add(tv);
        } else {
            for (Object[] objArray : cartaoValores) {
                String nomeCarta = ((String) objArray[0] == null ? UtilMetodos.getResourceBundle("semInformacao", FacesContext.getCurrentInstance()) : (String) objArray[0]);
                Double valorCarta = ((Double) objArray[1] == null ? 0.00 : (Double) objArray[1]);
                TipoValor tv = new TipoValor(nomeCarta, valorCarta);
                acumulado.add(tv);
            }
        }
        if (loop == 0) {
            acumulado.setMesAtual(true);
        }
        return acumulado;
    }

    public DataModel getCartaoDataModel() {
        if (this.cartaoDataModel == null) {
            atualizaValoresCartao(null);
        }
        return cartaoDataModel;
    }

    public void setCartaoDataModel(DataModel cartaoDataModel) {
        this.cartaoDataModel = cartaoDataModel;
    }

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

    public Date getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(Date dataReferencia) {
        this.dataReferencia = dataReferencia;
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

    public CartaoCreditoUnico getCartaoCreditoUnico() {
        return cartaoCreditoUnico;
    }

    public void setCartaoCreditoUnico(CartaoCreditoUnico cartaoCreditoUnico) {
        this.cartaoCreditoUnico = cartaoCreditoUnico;
    }

    public Date getMesCartao() {
        return mesCartao;
    }

    public void setMesCartao(Date mesCartao) {
        this.mesCartao = mesCartao;
    }

    public double getTotalFatura() {
        return totalFatura;
    }

    public void setTotalFatura(double totalFatura) {
        this.totalFatura = totalFatura;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public DataModel getDataModelContas() {
        return dataModelContas;
    }

    public void setDataModelContas(DataModel dataModelContas) {
        this.dataModelContas = dataModelContas;
    }

    public String getPareto(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ParetoJfreeChart pareto = new ParetoJfreeChart();
        SimpleDateFormat sd = new SimpleDateFormat("MMMM", locale);
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
        deletaFiles(this.proprietario.getId() + "_" + fileName + ".png");
        return "/temp/" + this.proprietario.getId() + "_" + fileName + ".png";
    }

    private void carregaMapPareto(List<ContaPagar> listContas){
        for(ContaPagar cp : listContas){
            if(mapPareto.containsKey(cp.getGrupoGasto().getGrupoGasto())){
                double tmp = mapPareto.get(cp.getGrupoGasto().getGrupoGasto());
                mapPareto.remove(cp.getGrupoGasto().getGrupoGasto());
                mapPareto.put(cp.getGrupoGasto().getGrupoGasto(), cp.getContaValor() + tmp);
            }else{
                mapPareto.put(cp.getGrupoGasto().getGrupoGasto(), cp.getContaValor());
            }
        }
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

    public void update(Observable o, Object arg) {
        mapPareto = new LinkedHashMap<String, Double>();
        atualizaValoresCartao(null);
        atualizarListaContasCartaoCredito(null);
    }

}
