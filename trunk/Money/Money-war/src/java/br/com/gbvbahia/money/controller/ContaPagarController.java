/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.controller;

import br.com.gbvbahia.money.controller.util.PaginationHelper;
import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.manager.SelectItemManager;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.ReceitaDivida;
import java.util.Date;
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

/**
 *
 * @author Guilherme
 */
@ManagedBean(name = "contaPagarController")
@ViewScoped
public class ContaPagarController {

    public static final String CONTA_PAGAR_RETURN = "mantem";
    public static final String CONTA_PAGAR_CHANGE = "contas";
    
    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    
    private ReceitaDivida current;
    private DataModel<ReceitaDivida> items = null;
    private PaginationHelper pagination;
    private boolean salvarParcelas = false;
    private boolean apagarPrestacoes = false;
    
    /**
     * Creates a new instance of ContaPagarController
     */
    public ContaPagarController() {
    }

    //====================
    //Iniciadores
    //====================
    //====================
    //Métodos de Negócio
    //====================
    public void clean() {
        this.current = null;
        apagarPrestacoes = false;
        salvarParcelas = false;
    }

    public String prepareList() {
        recreateModel();
        clean();
        return CONTA_PAGAR_RETURN;
    }
    
   public String prepareCreate() {
        current = new ReceitaDivida();
        current.setUsuario(this.loginManager.getUsuario());
        current.setDataVencimento(new Date());
        current.setTipoMovimentacao(TipoMovimentacao.RETIRADA);
        current.setStatusPagamento(StatusPagamento.NAO_PAGA);
        current.setValor(0.00);
        current.setParcelaAtual(1);
        current.setParcelaTotal(1);
        salvarParcelas = false;
        return CONTA_PAGAR_RETURN;
    }
     
    public String create() {
        try {
            if (!salvarParcelas) {
                this.receitaDividaBean.salvarReceitaDivida(current);
            } else {
                this.receitaDividaBean.salvarReceitaDivida(current, current.getParcelaTotal(),salvarParcelas, null, StatusPagamento.NAO_PAGA);
            }
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_CONTA_PAGAR_RECEBER);
            UtilMetodos.messageFactoringFull("contaPagarSalva", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
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
    
    public String prepareEdit() {
        current = (ReceitaDivida) getItems().getRowData();
        return CONTA_PAGAR_RETURN;
    }
    
    public String destroy() {
        current = (ReceitaDivida) getItems().getRowData();
        performDestroy();
        recreatePagination();
        recreateModel();
        return prepareList();
    }
    
    public void next() {
        getPagination().nextPage();
        recreateModel();
    }

    public void previous() {
        getPagination().previousPage();
        recreateModel();
    }
     
    //====================
    //Métodos Privados
    //====================
    private void performDestroy() {
        try {
            this.receitaDividaBean.apagarReceitaDivida(current, apagarPrestacoes);
            UtilMetodos.messageFactoringFull("contaApagadaOK", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            clean();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
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
    
     public String prepareDestroy(){
         current = (ReceitaDivida) getItems().getRowData();
         apagarPrestacoes = false;
        return CONTA_PAGAR_RETURN;
     }
     
    //====================
    //Select Itens
    //====================
    public List<SelectItem> getDetalhes() {
        return selectItemManager.getDetalhesUsuario(loginManager.getUsuario(), true, TipoMovimentacao.RETIRADA);
    }
     
    //====================
    //Getters AND Setters
    //====================
   
    public DataModel<ReceitaDivida> getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
        
    public ReceitaDividaBeanLocal getFacade() {
        return receitaDividaBean;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(5) {

                @Override
                public int getItemsCount() {
                    return getFacade().buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(loginManager.getUsuario(), StatusPagamento.NAO_PAGA, TipoMovimentacao.RETIRADA);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarReceitaDividasPorUsuarioStatusPaginada(getPageFirstItem(), getPageSize(), loginManager.getUsuario(), StatusPagamento.NAO_PAGA, TipoMovimentacao.RETIRADA));
                }
            };
        }
        return pagination;
    }

    public SelectItemManager getSelectItemManager() {
        return selectItemManager;
    }

    public void setSelectItemManager(SelectItemManager selectItemManager) {
        this.selectItemManager = selectItemManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public ReceitaDivida getCurrent() {
        return current;
    }

    public void setCurrent(ReceitaDivida current) {
        this.current = current;
    }
}
