/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.cadastro;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
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
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

/**
 * CRUD Usuário.
 *
 * @author Guilherme
 * @since 2012/02/22
 */
@ManagedBean
@ViewScoped
public class DetalheController extends EntityController<DetalheProcedimento>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(DetalheController.class);
    /**
     * Facade injetado pelo Contâiner para CRUD com usuário.
     */
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private DetalheProcedimentoFacade detalheFacade;
    private DetalheProcedimento current;

    /**
     * Construtor padrão.
     */
    public DetalheController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...DetalheController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...DetalheController");
    }

    //====================
    //Métodos Sobrescritos
    //====================
    @Override
    public String clean() {
        super.clean();
        return JsfUtil.MANTEM;
    }

    @Override
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {
                @Override
                public int getItemsCount() {
                    return getFacade().countarDetalhePorUsuario(usuarioFacade.getUsuario()).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().
                            buscarDetalhePorUserPaginado(usuarioFacade.getUsuario(),
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    @Override
    protected String create() {
        try {
            getFacade().create(current);
            MensagemUtils.messageFactoringFull("DetalheCreated",
                    new Object[]{current.getDetalhe()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("createError", current.toString()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected String update() {
        try {
            getFacade().update(current);
            MensagemUtils.messageFactoringFull("DetalheUpdated",
                    new Object[]{current.getDetalhe()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("updateError", current.toString()));
            return JsfUtil.MANTEM;
        }
    }

    @Override
    protected void performDestroy() {
        try {
            getFacade().remove(current);
            MensagemUtils.messageFactoringFull("DetalheDeleted",
                    new Object[]{current.getDetalhe()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("removeError", current.toString()));
        }
    }

    @Override
    public void setEntity(final DetalheProcedimento t) {
        this.current = t;
    }

    @Override
    protected DetalheProcedimento getNewEntity() {
        DetalheProcedimento det = new DetalheProcedimento();
        det.setUsuario(usuarioFacade.getUsuario());;
        return det;
    }

    //====================
    //Métodos Filtros Paginação
    //====================
    //====================
    // Select Itens
    //====================
    public SelectItem[] getTipos() {
        return JsfUtil.getEnumSelectItems(TipoProcedimento.class, false,
                FacesContext.getCurrentInstance());
    }

    //====================
    //Getters AND Setters
    //====================
    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public DetalheProcedimentoFacade getFacade() {
        return detalheFacade;
    }

    /**
     * Usuário atual, pode ser null, pode ser um novo ou um já existente.
     *
     * @return Usuario
     */
    public DetalheProcedimento getCurrent() {
        return current;
    }
}
