/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.manager.lazyTables.LazyMovimentacaoFinanceiraModel;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.MovimentacaoFinanceira;
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
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "movimentacaoFinanceiraDesfazerManager")
@SessionScoped
public class MovimentacaoFinanceiraDesfazerManager implements InterfaceManager, Observer {

    @EJB
    private MovimentacaoFinanceiraBeanLocal movimentacaoFinanceiraBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    private LazyDataModel<MovimentacaoFinanceira> movimentacoes;
    private MovimentacaoFinanceira movimentacaoFinanceiraSelecionada;

    public MovimentacaoFinanceiraDesfazerManager() {
    }

    //====================
    // Iniciadores
    //====================
    @PreDestroy
    @Override
    public void end() {
        ControleObserver.removeBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "MovimentacaoFinanceiraDesfazerManager.end() executado!");
    }

    @PostConstruct
    @Override
    public void init() {
        clean();
        this.movimentacoes = new LazyMovimentacaoFinanceiraModel(movimentacaoFinanceiraBean, loginManager.getUsuario());
        ControleObserver.addBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "MovimentacaoFinanceiraDesfazerManager.init() executado!");
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
        this.movimentacaoFinanceiraSelecionada = null;
    }

    //====================
    //Table Actions
    //====================
    public void desfazerReceitaDivida() {
        try {
            movimentacaoFinanceiraBean.desfazerMovimentacaoFinanceira(movimentacaoFinanceiraSelecionada);
            clean();
            UtilMetodos.messageFactoringFull("movimentacaoDesRealizadaOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_PAGAR_RECEBER, ControleObserver.Eventos.CAD_CONTA_BANCARIA, ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO);
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
    public List<SelectItem> getContasTableLabel(){
        return this.selectItemManager.getContaBancariaToTable(loginManager.getUsuario(), FacesContext.getCurrentInstance());
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

    public LazyDataModel<MovimentacaoFinanceira> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(LazyDataModel<MovimentacaoFinanceira> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public MovimentacaoFinanceira getMovimentacaoFinanceiraSelecionada() {
        return movimentacaoFinanceiraSelecionada;
    }

    public void setMovimentacaoFinanceiraSelecionada(MovimentacaoFinanceira movimentacaoFinanceiraSelecionada) {
        this.movimentacaoFinanceiraSelecionada = movimentacaoFinanceiraSelecionada;
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
