/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.pesquisa;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import br.com.gbvbahia.financeiro.modelos.superclass.MovimentacaoFinanceira;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.projeto.web.constante.Meses;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
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

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class ExtratoPesquisa {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(ExtratoPesquisa.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private MovimentacaoFinanceiraFacade movimentacaoFacade;
    @EJB
    private ContaBancariaFacade disponivelFacade;
    //SelectItem
    private List<Integer> listAnosSelect = new ArrayList<Integer>();
    private Meses mesOperacao;
    private Integer anoOperacao;
    //Info tela
    private List<MovimentacaoFinanceira> movimentacoes;
    private ContaBancaria conta;

    /**
     * Creates a new instance of ExtratoPesquisa
     */
    public ExtratoPesquisa() {
    }
    //====================
    //Iniciadores
    //====================

    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...ExtratoPesquisa");
        dataListener();
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...ExtratoPesquisa");
    }

    public void dataListener() {
        MinMaxDateDTO intervalodDatas = movimentacaoFacade.buscarIntervalodDatas(usuarioFacade.getUsuario());
        listAnosSelect = intervalodDatas.intervaloMinMaxAnos();
        if (listAnosSelect.isEmpty()) {
            listAnosSelect.add(DateUtils.getFieldDate(new Date(), Calendar.YEAR));
        }
    }

    public void buscarMovimentacoes() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, anoOperacao);
        c.set(Calendar.MONTH, mesOperacao.getMes());
        final Date[] intervalo = DateUtils.getIntervalo(c.getTime());
        movimentacoes = movimentacaoFacade.pesquisarMovimentacaoPorPeriodo(intervalo, conta);
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

    public SelectItem[] getContas() {
        return JsfUtil.getSelectItems(disponivelFacade.findAll(usuarioFacade.getUsuario(),
                Boolean.TRUE), true, FacesContext.getCurrentInstance());
    }

    //===================
    //Getter and Setters
    //===================
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

    public List<MovimentacaoFinanceira> getMovimentacoes() {
        if (movimentacoes == null) {
            movimentacoes = new ArrayList<MovimentacaoFinanceira>();
        }
        return movimentacoes;
    }

    public void setMovimentacoes(List<MovimentacaoFinanceira> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public ContaBancaria getConta() {
        return conta;
    }

    public void setConta(ContaBancaria conta) {
        this.conta = conta;
    }
}
