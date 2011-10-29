/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "contaBancariaManager")
@ViewScoped
public class ContaBancariaManager implements InterfaceManager, Observer {
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    
    private ContaBancaria contaBancaria;
    
     private HtmlInputText contaInput;
     private HtmlInputText saldoInput;
    private HtmlSelectOneMenu selctTipoConta;
    
    /** Creates a new instance of ContaBancariaManager */
    public ContaBancariaManager() {
    }

    //====================
    // Iniciadores
    //====================
    @PreDestroy
    @Override
    public void end() {
        ControleObserver.removeBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "ContaBancariaManager.end() executado!");
    }

    @PostConstruct
    @Override
    public void init() {
        clean();
        ControleObserver.addBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "ContaBancariaManager.init() executado!");
    }

    //====================
    //Métodos de Negócio
    //====================
    
    public void salvarContaBancaria(){
        try{
            contaBancariaBean.salvarContaBancaria(contaBancaria);
            UtilMetodos.messageFactoringFull("contaBancariaCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO);
            clean();
        }catch(ValidacaoException v){
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        int[] args = (int[]) arg;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == ControleObserver.Eventos.CAD_CONTA_BANCARIA) {
                //atualizarModel();
            }
        }
    }
    
    public void clean(){
        contaBancaria = new ContaBancaria();
        contaBancaria.setUser(loginManager.getUsuario());
        if(contaInput != null){
            contaInput.setSubmittedValue("");
        }
        if(saldoInput != null){
            saldoInput.setSubmittedValue("0,00");
        }
        if(selctTipoConta != null){
            selctTipoConta.setSubmittedValue(UtilMetodos.getResourceBundle("selecione", FacesContext.getCurrentInstance()));
            selctTipoConta.setValue(null);
        }
    }
    //====================
    //Table Actions
    //====================
    //====================
    //SelectItem
    //====================
    public List<SelectItem> getSelectTipoConta(){
        return this.selectItemManager.getLinguagens();
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

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public SelectItemManager getSelectItemManager() {
        return selectItemManager;
    }

    public void setSelectItemManager(SelectItemManager selectItemManager) {
        this.selectItemManager = selectItemManager;
    }

    public HtmlInputText getContaInput() {
        return contaInput;
    }

    public void setContaInput(HtmlInputText contaInput) {
        this.contaInput = contaInput;
    }

    public HtmlSelectOneMenu getSelctTipoConta() {
        return selctTipoConta;
    }

    public void setSelctTipoConta(HtmlSelectOneMenu selctTipoConta) {
        this.selctTipoConta = selctTipoConta;
    }

    public HtmlInputText getSaldoInput() {
        return saldoInput;
    }

    public void setSaldoInput(HtmlInputText saldoInput) {
        this.saldoInput = saldoInput;
    }
}
