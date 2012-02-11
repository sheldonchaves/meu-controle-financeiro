/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.manager.lazyTables.LazyTransferenciaEntreContas;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.MovimentacaoFinanceira;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;
/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "transferenciaEntreContasManager")
@ViewScoped
public class TransferenciaEntreContasManager implements InterfaceManager {
    @EJB
    private MovimentacaoFinanceiraBeanLocal movimentacaoFinanceiraBean;

    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;

    private ContaBancaria contaDebitar;
    private ContaBancaria contaCreditar;
    private Double valor;
    private LazyDataModel<MovimentacaoFinanceira> transferencias;
    
    private HtmlInputText valorInput;
    private HtmlSelectOneMenu selctDebitarDe;
    private HtmlSelectOneMenu selctTransferirPara;
    
    /** Creates a new instance of TransferenciaEntreContasManager */
    public TransferenciaEntreContasManager() {
    }
    //====================
    // Iniciadores
    //====================

    @Override
    @PreDestroy
    public void end() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, this.getClass().getName() + ".end() executado!");
    }

    @Override
    @PostConstruct
    public void init() {
        this.transferencias = new LazyTransferenciaEntreContas(movimentacaoFinanceiraBean, this.loginManager.getUsuario());
        clean();
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, this.getClass().getName() + ".init() executado!");
    }
    //====================
    //Métodos de Negócio
    //====================
    public void clean(){
        contaCreditar = null;
        contaDebitar = null;
        
        if(valorInput != null){
            valorInput.setSubmittedValue("0,00");
        }
        
        if(selctDebitarDe != null){
            selctDebitarDe.setSubmittedValue(UtilMetodos.getResourceBundle("selecione", FacesContext.getCurrentInstance()));
            selctDebitarDe.setValue(null);
        }
        
        if(selctTransferirPara != null){
            selctTransferirPara.setSubmittedValue(UtilMetodos.getResourceBundle("selecione", FacesContext.getCurrentInstance()));
            selctTransferirPara.setValue(null);
        }
    }
    
    public void transferir(){
        try{
        this.movimentacaoFinanceiraBean.realizarTransferenciaEntreContas(contaDebitar, contaCreditar, valor);
        ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_BANCARIA);
        UtilMetodos.messageFactoringFull("TransferenciaSalva", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        clean();
        }catch(ValidacaoException v){
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
    
    //====================
    //SelectItem
    //====================
    public List<SelectItem> getContasToSelect(){
        return this.selectItemManager.getContaBancaria(loginManager.getUsuario());
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

    public ContaBancaria getContaCreditar() {
        return contaCreditar;
    }

    public void setContaCreditar(ContaBancaria contaCreditar) {
        this.contaCreditar = contaCreditar;
    }

    public ContaBancaria getContaDebitar() {
        return contaDebitar;
    }

    public void setContaDebitar(ContaBancaria contaDebitar) {
        this.contaDebitar = contaDebitar;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public HtmlInputText getValorInput() {
        return valorInput;
    }

    public void setValorInput(HtmlInputText valorInput) {
        this.valorInput = valorInput;
    }

    public HtmlSelectOneMenu getSelctDebitarDe() {
        return selctDebitarDe;
    }

    public void setSelctDebitarDe(HtmlSelectOneMenu selctDebitarDe) {
        this.selctDebitarDe = selctDebitarDe;
    }

    public HtmlSelectOneMenu getSelctTransferirPara() {
        return selctTransferirPara;
    }

    public void setSelctTransferirPara(HtmlSelectOneMenu selctTransferirPara) {
        this.selctTransferirPara = selctTransferirPara;
    }

    public LazyDataModel<MovimentacaoFinanceira> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(LazyDataModel<MovimentacaoFinanceira> transferencias) {
        this.transferencias = transferencias;
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
