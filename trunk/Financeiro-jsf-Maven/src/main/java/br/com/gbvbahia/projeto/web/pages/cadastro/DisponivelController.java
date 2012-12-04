/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.cadastro;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.TipoConta;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.math.BigDecimal;
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
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class DisponivelController extends EntityController<ContaBancaria>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(DisponivelController.class);
    /**
     * Facade injetado pelo Contâiner para CRUD com usuário.
     */
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ContaBancariaFacade disponivelFacade;
    private ContaBancaria current;
    private boolean saldoNegativo;

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
                    return getFacade().contarContasUsuario(usuarioFacade.getUsuario()).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarContasUsuarioPaginado(usuarioFacade.getUsuario(),
                            new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    @Override
    protected String create() {
        try {
            if(saldoNegativo){
                current.setSaldo(current.getSaldo().multiply(new BigDecimal("-1")));
            }
            getFacade().create(current);
            MensagemUtils.messageFactoringFull("ContaCreated",
                    new Object[]{current.getNomeConta()},
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
            MensagemUtils.messageFactoringFull("ContaUpdated",
                    new Object[]{current.getNomeConta()},
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
            MensagemUtils.messageFactoringFull("ContaDeleted",
                    new Object[]{current.getNomeConta()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("removeError", current.toString()));
        }
        clean();
    }

    public void performBlock() {
        try {
            setEntity(getItems().getRowData());
            current.setStatus(!current.isStatus());
            getFacade().update(current);
            MensagemUtils.messageFactoringFull(current.isStatus() ? "ContaAtivada" : "ContaDestivada",
                    new Object[]{current.getNomeConta()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            clean();
            recreateTable();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("removeError", current.toString()));
        }
    }

    @Override
    public void setEntity(final ContaBancaria t) {
        this.current = t;
    }

    @Override
    protected ContaBancaria getNewEntity() {
        ContaBancaria det = new ContaBancaria();
        det.setUsuario(usuarioFacade.getUsuario());
        return det;
    }

    //====================
    // Select Itens
    //====================
    public SelectItem[] getTipos() {
        return JsfUtil.getEnumSelectItems(TipoConta.class, true,
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
    public ContaBancariaFacade getFacade() {
        return disponivelFacade;
    }

    /**
     * Usuário atual, pode ser null, pode ser um novo ou um já existente.
     *
     * @return Usuario
     */
    public ContaBancaria getCurrent() {
        return current;
    }

    public boolean isSaldoNegativo() {
        return saldoNegativo;
    }

    public void setSaldoNegativo(boolean saldoNegativo) {
        this.saldoNegativo = saldoNegativo;
    }
}
