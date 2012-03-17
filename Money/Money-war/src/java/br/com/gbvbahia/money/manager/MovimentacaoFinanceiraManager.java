/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.manager.lazyTables.LazyReceitaDividaModel;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.selectonelistbox.SelectOneListbox;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "movimentacaoFinanceiraManager")
@SessionScoped
public class MovimentacaoFinanceiraManager implements Observer {

    @EJB
    private MovimentacaoFinanceiraBeanLocal movimentacaoFinanceiraBean;
    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    private LazyDataModel<ReceitaDivida> receitasDividas;
    private ReceitaDivida receitaDivitaSelecionada;
    private SelectOneListbox selctContaPagamento;

    public MovimentacaoFinanceiraManager() {
    }

    //====================
    // Iniciadores
    //====================
    @PreDestroy
    public void end() {
        ControleObserver.removeBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, this.getClass().getName() + ".end() executado!");
    }

    @PostConstruct
    public void init() {
        clean();
        this.receitasDividas = new LazyReceitaDividaModel(receitaDividaBean, loginManager.getUsuario(), StatusPagamento.NAO_PAGA, null);
        ControleObserver.addBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, this.getClass().getName() + ".init() executado!");
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
            }
        }
    }

    public void clean() {
        this.receitaDivitaSelecionada = new ReceitaDivida();
    }

    //====================
    //Table Actions
    //====================
    public void quitarReceitaDivida() {
        if (this.receitaDivitaSelecionada == null
                || this.receitaDivitaSelecionada.getContaBancariaTransiente() == null
                || this.receitaDivitaSelecionada.getContaBancariaTransiente().getId() == null) {
            MensagemUtils.messageFactoringFull("movimentacaoRealizadaContaNOk",
                    null, FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            return;
        }
        try {
            this.movimentacaoFinanceiraBean.salvarMovimentacaoFinanceira(
                    receitaDivitaSelecionada.getContaBancariaTransiente(),
                    receitaDivitaSelecionada);
            clean();
            MensagemUtils.messageFactoringFull("movimentacaoRealizadaOk", null,
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_PAGAR_RECEBER, ControleObserver.Eventos.CAD_CONTA_BANCARIA, ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO);
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                MensagemUtils.messageFactoringFull(
                        MensagemUtils.getResourceBundle(v.getMessage(),
                        FacesContext.getCurrentInstance()), null, 
                        FacesMessage.SEVERITY_ERROR, 
                        FacesContext.getCurrentInstance());
            } else {
                MensagemUtils.messageFactoringFull(v.getMessage(), null,
                        FacesMessage.SEVERITY_ERROR,
                        FacesContext.getCurrentInstance());
            }
        }
    }

    //====================
    //SelectItem
    //====================
    public List<SelectItem> getContasBancarias() {
        return selectItemManager.getContaBancaria(loginManager.getUsuario());
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

    public LazyDataModel<ReceitaDivida> getReceitasDividas() {
        return receitasDividas;
    }

    public void setReceitasDividas(LazyDataModel<ReceitaDivida> receitasDividas) {
        this.receitasDividas = receitasDividas;
    }

    public ReceitaDivida getReceitaDivitaSelecionada() {
        return receitaDivitaSelecionada;
    }

    public void setReceitaDivitaSelecionada(ReceitaDivida receitaDivitaSelecionada) {
        this.receitaDivitaSelecionada = receitaDivitaSelecionada;
    }

    public SelectOneListbox getSelctContaPagamento() {
        return selctContaPagamento;
    }

    public void setSelctContaPagamento(SelectOneListbox selctContaPagamento) {
        this.selctContaPagamento = selctContaPagamento;
    }

    public Locale getLocale() {
        return SelectItemManager.BRASIL;
    }

    public String getPattern() {
        return SelectItemManager.PATTERN;
    }
}
