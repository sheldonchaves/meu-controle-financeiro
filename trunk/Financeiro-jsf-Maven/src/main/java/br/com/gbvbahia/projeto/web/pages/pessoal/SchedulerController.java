/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.pessoal;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.SchedulerFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.Scheduler;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class SchedulerController {

    @EJB
    private SchedulerFacade schedulerFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    private Scheduler current;
    private Logger logger = Logger.getLogger(SchedulerController.class);

    /**
     * Creates a new instance of SchedulerController
     */
    public SchedulerController() {
    }
    //====================
    // Iniciadores        
    //====================

    @PreDestroy
    public void end() {
        logger.info("end()...SchedulerController");
    }

    @PostConstruct
    public void init() {
        clean();
        logger.info("init()...SchedulerController");
    }
    //====================
    //Métodos de Negócio  
    //====================

    public void clean() {
        this.current = schedulerFacade.buscarSchedulerPorUsuario(
                usuarioFacade.getUsuario());
        if (this.current == null) {
            this.current = new Scheduler();
            this.current.setDias(2);
            this.current.setEmail(this.usuarioFacade.getUsuario().getEmail());
            this.current.setStatus(false);
            this.current.setUser(usuarioFacade.getUsuario());
        }
    }

    public void salvarScheduler() {
        try {
            this.schedulerFacade.create(current);
            MensagemUtils.messageFactoringFull("schedulerSalvo", null,
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("updateError", current.toString()));
        }
    }

    public Scheduler getCurrent() {
        return current;
    }

    public void setCurrent(Scheduler current) {
        this.current = current;
    }
    
}
