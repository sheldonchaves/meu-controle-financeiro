/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.manager.lazyTables.LazyReceitaDividaModel;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedProperty;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "contaPagarManager")
@SessionScoped
public class ContaPagarManager implements InterfaceManager, Observer {

    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    private LazyDataModel<ReceitaDivida> dividas;//LazyLoad (Paginação)
    private List<ReceitaDivida> temp;
    private ReceitaDivida receitaDivida;
    private boolean salvarParcelas;
    private ReceitaDivida receitaDividaToDelete;
    private boolean apagarPrestacoes;
    private HtmlInputText valorInput;
    private HtmlInputText parcelAtualInput;
    private HtmlInputText parcelTotalInput;
    private HtmlInputText obsInut;
    private HtmlSelectBooleanCheckbox salvarParcelasInput;
    private HtmlSelectOneMenu selctDetalhePagamento;
    private org.primefaces.component.calendar.Calendar calendarInput;

    public ContaPagarManager() {
    }
    //====================
    // Iniciadores
    //====================

    @PostConstruct
    @Override
    public void init() {
        clean();
        this.dividas = new LazyReceitaDividaModel(receitaDividaBean, loginManager.getUsuario(), StatusPagamento.NAO_PAGA, TipoMovimentacao.RETIRADA);
        temp = this.receitaDividaBean.buscarReceitaDividasPorUsuarioStatusPaginada(0, 10, loginManager.getUsuario(), StatusPagamento.NAO_PAGA, TipoMovimentacao.RETIRADA);
        ControleObserver.addBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "ContaPagarManager.init() executado!");
    }

    @PreDestroy
    @Override
    public void end() {
        ControleObserver.removeBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "ContaPagarManager.end() executado!");
    }

    //====================
    //Métodos de Negócio
    //====================
    @Override
    public void update(Observable o, Object arg) {
        int[] args = (int[]) arg;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == ControleObserver.Eventos.CAD_CONTA_PAGAR_RECEBER) {
                //atualizarModel();
                //Nada é feito ja que a tabela é carrega sob demanada Lazy...
            }
        }
    }

    public void clean() {
        receitaDivida = new ReceitaDivida();
        receitaDivida.setUsuario(loginManager.getUsuario());
        receitaDivida.setDataVencimento(new Date());
        receitaDivida.setTipoMovimentacao(TipoMovimentacao.RETIRADA);
        receitaDivida.setStatusPagamento(StatusPagamento.NAO_PAGA);
        receitaDivida.setValor(0.00);
        receitaDivida.setParcelaAtual(1);
        receitaDivida.setParcelaTotal(1);
        salvarParcelas = false;
        receitaDividaToDelete = null;
        apagarPrestacoes = false;
        if (valorInput != null) {
            valorInput.setSubmittedValue("0,00");
        }
        if (calendarInput != null) {
            calendarInput.setSubmittedValue(UtilMetodos.getDataString(new Date()));
            calendarInput.setValue(new Date());
        }
        if (parcelAtualInput != null) {
            parcelAtualInput.setSubmittedValue("1");
        }
        if (parcelTotalInput != null) {
            parcelTotalInput.setSubmittedValue("1");
        }
        if (obsInut != null) {
            obsInut.setSubmittedValue("");
        }
        if (salvarParcelasInput != null) {
            salvarParcelasInput.setSelected(false);
        }
        if (selctDetalhePagamento != null) {
            selctDetalhePagamento.setSubmittedValue(UtilMetodos.getResourceBundle("selecione", FacesContext.getCurrentInstance()));
            selctDetalhePagamento.setValue(null);
        }
    }

    public void salvarContaPagar() {
        try {
            if (!salvarParcelas) {
                this.receitaDividaBean.salvarReceitaDivida(receitaDivida);
            } else {
                this.receitaDividaBean.salvarReceitaDivida(receitaDivida, receitaDivida.getParcelaTotal(),salvarParcelas, null, StatusPagamento.NAO_PAGA);
            }
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_PAGAR_RECEBER);
            UtilMetodos.messageFactoringFull("contaPagarSalva", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            clean();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
        }
    }

    //====================
    //Table Actions
    //====================
    public void deletarConta() {
        try {
            this.receitaDividaBean.apagarReceitaDivida(receitaDividaToDelete, apagarPrestacoes);
            UtilMetodos.messageFactoringFull("contaApagadaOK", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            clean();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
        }
    }

    //====================
    //SelectItem
    //====================
    public List<SelectItem> getDetalhes() {
        return selectItemManager.getDetalhesUsuario(loginManager.getUsuario(), true, TipoMovimentacao.RETIRADA);
    }
    //=========================
    //Getters AND Setters
    //=========================

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public SelectItemManager getSelectItemManager() {
        return selectItemManager;
    }

    public void setSelectItemManager(SelectItemManager selectItemManager) {
        this.selectItemManager = selectItemManager;
    }

    public ReceitaDivida getReceitaDivida() {
        return receitaDivida;
    }

    public void setReceitaDivida(ReceitaDivida receitaDivida) {
        this.receitaDivida = receitaDivida;
        this.obsInut.setSubmittedValue(receitaDivida.getObservacao());
        this.salvarParcelasInput.setSelected(false);
        this.salvarParcelas = false;
        this.parcelTotalInput.setSubmittedValue(receitaDivida.getParcelaTotal());
        this.parcelAtualInput.setSubmittedValue(receitaDivida.getParcelaAtual());
        this.valorInput.setSubmittedValue(UtilMetodos.getNumberFormater().format(receitaDivida.getValor()));
        this.calendarInput.setPattern(this.getPattern());
        this.calendarInput.setSubmittedValue(UtilMetodos.getDataString(receitaDivida.getDataVencimento()));

    }

    public boolean isSalvarParcelas() {
        return salvarParcelas;
    }

    public void setSalvarParcelas(boolean salvarParcelas) {
        this.salvarParcelas = salvarParcelas;
    }

    public HtmlInputText getValorInput() {
        return valorInput;
    }

    public void setValorInput(HtmlInputText valorInput) {
        this.valorInput = valorInput;
    }

    public org.primefaces.component.calendar.Calendar getCalendarInput() {
        return calendarInput;
    }

    public void setCalendarInput(org.primefaces.component.calendar.Calendar calendarInput) {
        this.calendarInput = calendarInput;
    }

    public HtmlInputText getParcelAtualInput() {
        return parcelAtualInput;
    }

    public void setParcelAtualInput(HtmlInputText parcelAtualInput) {
        this.parcelAtualInput = parcelAtualInput;
    }

    public HtmlInputText getParcelTotalInput() {
        return parcelTotalInput;
    }

    public void setParcelTotalInput(HtmlInputText parcelTotalInput) {
        this.parcelTotalInput = parcelTotalInput;
    }

    public HtmlInputText getObsInut() {
        return obsInut;
    }

    public void setObsInut(HtmlInputText obsInut) {
        this.obsInut = obsInut;
    }

    public HtmlSelectBooleanCheckbox getSalvarParcelasInput() {
        return salvarParcelasInput;
    }

    public void setSalvarParcelasInput(HtmlSelectBooleanCheckbox salvarParcelasInput) {
        this.salvarParcelasInput = salvarParcelasInput;
    }

    public LazyDataModel<ReceitaDivida> getDividas() {
        return dividas;
    }

    public void setDividas(LazyDataModel<ReceitaDivida> dividas) {
        this.dividas = dividas;
    }

    public List<ReceitaDivida> getTemp() {
        return temp;
    }

    public void setTemp(List<ReceitaDivida> temp) {
        this.temp = temp;
    }

    public boolean isApagarPrestacoes() {
        return apagarPrestacoes;
    }

    public void setApagarPrestacoes(boolean apagarPrestacoes) {
        this.apagarPrestacoes = apagarPrestacoes;
    }

    public ReceitaDivida getReceitaDividaToDelete() {
        return receitaDividaToDelete;
    }

    public void setReceitaDividaToDelete(ReceitaDivida receitaDividaToDelete) {
        this.receitaDividaToDelete = receitaDividaToDelete;
    }

    public HtmlSelectOneMenu getSelctDetalhePagamento() {
        return selctDetalhePagamento;
    }

    public void setSelctDetalhePagamento(HtmlSelectOneMenu selctDetalhePagamento) {
        this.selctDetalhePagamento = selctDetalhePagamento;
    }

    @Override
    public Locale getLocale() {
        return SelectItemManager.BRASIL;
    }

    @Override
    public String getPattern() {
        return SelectItemManager.PATTERN;
    }
}