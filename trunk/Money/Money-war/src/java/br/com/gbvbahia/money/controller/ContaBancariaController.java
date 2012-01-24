package br.com.gbvbahia.money.controller;

import br.com.gbvbahia.money.controller.util.PaginationHelper;
import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.lang.StringUtils;

@ManagedBean(name = "contaBancariaController")
@ViewScoped
public class ContaBancariaController implements Serializable {

    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    private ContaBancaria current;
    private DataModel<ContaBancaria> items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ContaBancariaController() {
    }

    public ContaBancaria getSelected() {
        if (current == null) {
            current = new ContaBancaria();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ContaBancariaBeanLocal getFacade() {
        return contaBancariaBean;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(5) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (ContaBancaria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ContaBancaria();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().salvarContaBancaria(current);
            UtilMetodos.messageFactoringFull("contaBancariaCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_BANCARIA);
            return prepareCreate();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
            return null;
        }
    }

    public String prepareEdit() {
        current = (ContaBancaria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().salvarContaBancaria(current);
            UtilMetodos.messageFactoringFull("contaBancariaCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_BANCARIA);
            return "View";
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
            return null;
        }
    }

    public String destroy() {
        current = (ContaBancaria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
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

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel<ContaBancaria> getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public void next() {
        getPagination().nextPage();
        recreateModel();
    }

    public void previous() {
        getPagination().previousPage();
        recreateModel();
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }
}
