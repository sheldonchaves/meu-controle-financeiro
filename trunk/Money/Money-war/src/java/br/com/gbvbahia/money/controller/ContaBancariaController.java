package br.com.gbvbahia.money.controller;

import br.com.gbvbahia.money.controller.util.PaginationHelper;
import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.manager.SelectItemManager;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;

@ManagedBean(name = "contaBancariaController")
@ViewScoped
public class ContaBancariaController implements Serializable {

    public static final String CONTA_BANCARIA_RETURN = "mantem";
    public static final String CONTA_BANCARIA_CHANGE = "contasbancarias";
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    private ContaBancaria current;
    private DataModel<ContaBancaria> items = null;
    private PaginationHelper pagination;

    public ContaBancariaController() {
    }
    //====================
    //Iniciadores
    //====================

    //====================
    //Métodos de Negócio
    //====================
    public void clean() {
        this.current = null;
    }

    public void next() {
        getPagination().nextPage();
        recreateModel();
    }

    public void previous() {
        getPagination().previousPage();
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        current = null;
        return CONTA_BANCARIA_RETURN;
    }

    public String prepareCreate() {
        current = new ContaBancaria();
        current.setUser(this.loginManager.getUsuario());
        return CONTA_BANCARIA_RETURN;
    }

    public String create() {
        try {
            getFacade().salvarContaBancaria(current);
            UtilMetodos.messageFactoringFull("contaBancariaCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_BANCARIA);
            return prepareList();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
            return null;
        }
    }

    public String update() {
        try {
            getFacade().salvarContaBancaria(current);
            UtilMetodos.messageFactoringFull("contaBancariaCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_BANCARIA);
            return CONTA_BANCARIA_RETURN;
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
            return null;
        }
    }
    //====================
    //Métodos Privados
    //====================

    private ContaBancariaBeanLocal getFacade() {
        return contaBancariaBean;
    }

    private void performDestroy() {
        try {
            getFacade().apagarContaBancaria(current.getId());
            UtilMetodos.messageFactoringFull("contaBancariaDeleteOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_BANCARIA);
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
        } catch (Exception e) {
            UtilMetodos.messageFactoringFull(e.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            e.printStackTrace();
        }
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }
    
    //====================
    //Métodos em Tabelas
    //====================

    public String prepareEdit() {
        current = (ContaBancaria) getItems().getRowData();
        return CONTA_BANCARIA_RETURN;
    }

    public String destroy() {
        current = (ContaBancaria) getItems().getRowData();
        performDestroy();
        recreatePagination();
        recreateModel();
        return prepareList();
    }

    //====================
    //Select Itens
    //====================
    public List<SelectItem> getSelectTipoConta() {
        return this.selectItemManager.getLinguagens();
    }
    //====================
    //Getters AND Setters
    //====================

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(5) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, loginManager.getUsuario()));
                }
            };
        }
        return pagination;
    }

    public DataModel<ContaBancaria> getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public ContaBancaria getCurrent() {
        return current;
    }

    public void setCurrent(ContaBancaria current) {
        this.current = current;
    }

    public SelectItemManager getSelectItemManager() {
        return selectItemManager;
    }

    public void setSelectItemManager(SelectItemManager selectItemManager) {
        this.selectItemManager = selectItemManager;
    }
}
