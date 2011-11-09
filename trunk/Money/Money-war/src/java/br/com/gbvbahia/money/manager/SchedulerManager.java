/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.SchedulerBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Scheduler;
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
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "schedulerManager")
@ViewScoped
public class SchedulerManager implements InterfaceManager {
    
    @EJB
    private SchedulerBeanLocal schedulerBean;

    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    
    private Scheduler shScheduler;
    
    public SchedulerManager() {
    }
    
    //====================
    // Iniciadores        
    //====================
    @Override
    @PreDestroy
    public void end() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "SchedulerManager.end() executado!");
    }

    @Override
    @PostConstruct
    public void init() {
        clean();
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "SchedulerManager.init() executado!");
    }
    
    //====================
    //Métodos de Negócio  
    //====================
    public void clean(){
        this.shScheduler = schedulerBean.buscarSchedulerPorUsuario(loginManager.getUsuario());
        if(this.shScheduler == null){
            this.shScheduler = new Scheduler();
            shScheduler.setDias(2);
            shScheduler.setEmail(this.loginManager.getUsuario().getEmail());
            shScheduler.setStatus(false);
            shScheduler.setUser(loginManager.getUsuario());
        }
    }
    
    public void salvarScheduler(){
        try{
            this.schedulerBean.salvarScheduler(shScheduler);
            UtilMetodos.messageFactoringFull("schedulerSalvo", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
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
    //====================
    //Getters AND Setters 
    //====================

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public Scheduler getShScheduler() {
        return shScheduler;
    }

    public void setShScheduler(Scheduler shScheduler) {
        this.shScheduler = shScheduler;
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
