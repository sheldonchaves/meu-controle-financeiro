/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.pesquisa;

import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.projeto.web.constante.Meses;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.projeto.web.pages.report.utils.ClassificacaoMakePie;
import br.com.gbvbahia.projeto.web.pages.report.utils.DetalheMakePie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class DetalhePesquisa implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(DetalhePesquisa.class);
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private DetalheProcedimentoFacade detalheFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    //SelectItem
    private List<Integer> listAnosSelect = new ArrayList<Integer>();
    private Meses mesOperacao;
    private Integer anoOperacao;
    private DetalheProcedimento detalhe;
    //Info tela
    private List<DespesaProcedimento> procedimentos;
    private Double total;

    /**
     * Creates a new instance of DetalhePesquisa
     */
    public DetalhePesquisa() {
    }
    //====================
    //Iniciadores
    //====================

    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...DetalhePesquisa");
        dataListener();
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...DetalhePesquisa");
    }

    public void dataListener() {
        MinMaxDateDTO intervalodDatas = procedimentoFacade.buscarIntervalodDatas(null,
                null, usuarioFacade.getUsuario());
        listAnosSelect = intervalodDatas.intervaloMinMaxAnos();
        if (listAnosSelect.isEmpty()) {
            listAnosSelect.add(DateUtils.getFieldDate(new Date(), Calendar.YEAR));
        }
    }

    public void buscarDespesas() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, anoOperacao);
        c.set(Calendar.MONTH, mesOperacao.getMes());
        final Date[] intervalo = DateUtils.getIntervalo(c.getTime());
        procedimentos = procedimentoFacade.pesquisaDetalheProcedimento(usuarioFacade.getUsuario(), intervalo, detalhe);
        total = 0d;
        for (DespesaProcedimento dp : procedimentos) {
            total += dp.getValor().doubleValue();
        }
    }
//====================
// Select Itens
//====================

    public SelectItem[] getMeses() {
        return JsfUtil.getEnumSelectItems(Meses.class, false, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getAnos() {
        SelectItem[] anos = new SelectItem[listAnosSelect.size()];
        for (int i = 0; i < anos.length; i++) {
            anos[i] = new SelectItem(listAnosSelect.get(i), listAnosSelect.get(i).toString());
        }
        return anos;
    }

    public SelectItem[] getDetalhesSelect() {
        return JsfUtil.getSelectItems(detalheFacade.findAllDetalhe(usuarioFacade.getUsuario(),
                Boolean.TRUE, TipoProcedimento.DESPESA_FINANCEIRA), false, FacesContext.getCurrentInstance());
    }
    //===================
    //Getter and Setters
    //===================

    public PieChartModel getPieClassDetalhe() {
        PieChartModel pieClassDetalhe = new DetalheMakePie(getProcedimentos(), new PieChartModel(), FacesContext.getCurrentInstance()).makePie();
        return pieClassDetalhe;
    }

    public PieChartModel getPieClassModel() {
        PieChartModel pieClassModel = new ClassificacaoMakePie(getProcedimentos(),
                new PieChartModel(), FacesContext.getCurrentInstance()).makePie();
        return pieClassModel;
    }

    public Meses getMesOperacao() {
        return mesOperacao;
    }

    public void setMesOperacao(Meses mesOperacao) {
        this.mesOperacao = mesOperacao;
    }

    public Integer getAnoOperacao() {
        return anoOperacao;
    }

    public void setAnoOperacao(Integer anoOperacao) {
        this.anoOperacao = anoOperacao;
    }

    public List<DespesaProcedimento> getProcedimentos() {
        if (procedimentos == null) {
            procedimentos = new ArrayList<DespesaProcedimento>();
        }
        return procedimentos;
    }

    public void setProcedimentos(List<DespesaProcedimento> procedimentos) {
        this.procedimentos = procedimentos;
    }

    public DetalheProcedimento getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(DetalheProcedimento detalhe) {
        this.detalhe = detalhe;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
