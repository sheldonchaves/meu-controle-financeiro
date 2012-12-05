/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.admin.email;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.EmailPropertiesFacade;
import br.com.gbvbahia.financeiro.modelos.EmailProperties;
import br.com.gbvbahia.financeiro.utils.Encryption;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Guilherme
 */
@ManagedBean
@ViewScoped
public class EmailPropertiesController extends EntityController<EmailProperties>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(EmailPropertiesController.class);
    @EJB
    private EmailPropertiesFacade emailFacade;
    private EmailProperties current;

    /**
     * Creates a new instance of EmailPropertiesController
     */
    public EmailPropertiesController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...EmailPropertiesController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...EmailPropertiesController");
    }

    @Override
    protected void setEntity(EmailProperties t) {
        this.current = t;
        if(current != null && current.getSenhaEmail() != null){
            //logger.info("Senha criptografada: "+Encryption.encrypting(current.getSenhaEmail()));
            current.setSenhaEmail(Encryption.decrypting(current.getSenhaEmail()));
        }
    }

    @Override
    protected EmailProperties getNewEntity() {
        this.current = new EmailProperties();
        return current;
    }

    @Override
    protected void performDestroy() {
        try {
            getFacade().remove(current);
            MensagemUtils.messageFactoringFull("EmailDeleted",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("removeError", current.getId()));
        }
    }

    @Override
    protected String create() {
        try {
            current.setSenhaEmail(Encryption.encrypting(current.getSenhaEmail()));
            getFacade().create(current);
            MensagemUtils.messageFactoringFull("EmailCreated",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("createError", current.getId()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected String update() {
        try {
            current.setSenhaEmail(Encryption.encrypting(current.getSenhaEmail()));
            getFacade().update(current);
            MensagemUtils.messageFactoringFull("EmailUpdated",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("updateError", current.getId()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public EmailPropertiesFacade getFacade() {
        return emailFacade;
    }

    public EmailProperties getCurrent() {
        return current;
    }
}
