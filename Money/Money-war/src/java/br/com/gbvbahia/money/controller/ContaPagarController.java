/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.controller;

import br.com.gbvbahia.money.controller.util.JsfUtil;
import br.com.gbvbahia.money.controller.util.PaginationHelper;
import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.manager.SelectItemManager;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ReceitaDivida;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
public class ContaPagarController extends EntityController<ReceitaDivida> {

    public static final String CONTA_PAGAR_CHANGE = "contas";
    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    private ReceitaDivida current;
    private boolean salvarParcelas = false;
    private boolean apagarPrestacoes = false;

    /**
     * Data utilizada para filtro nas contas.
     */
    private Date dataVencimento;
    /**
     * String utilizada para filtro nas contas.
     */
    private String detalhe;
    /**
     * Creates a new instance of ContaPagarController
     */
    public ContaPagarController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado quando o bean JSF é instânciado.
     */
    @PostConstruct
    public void init() {
        Logger.getLogger(this.getClass().getName()).log(
                JsfUtil.LEVEL_LOG,
                "{0}.init()...", this.getClass().getName());
    }

    /**
     * Executado quando o bean JSF é destruído.
     */
    @PreDestroy
    public void end() {
        Logger.getLogger(this.getClass().getName()).log(
                JsfUtil.LEVEL_LOG,
                "{0}.end()...", this.getClass().getName());
    }
    //====================
    //Métodos de Negócio
    //====================

    @Override
    public String create() {
        try {
            if (!salvarParcelas) {
                this.receitaDividaBean.salvarReceitaDivida(current);
            } else {
                this.receitaDividaBean.salvarReceitaDivida(current,
                        current.getParcelaTotal(),
                        salvarParcelas, null,
                        StatusPagamento.NAO_PAGA);
            }
            ControleObserver.notificaObservers(loginManager.getUsuario(),
                    ControleObserver.Eventos.CAD_CONTA_PAGAR_RECEBER);
            MensagemUtils.messageFactoringFull("contaPagarSalva",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                MensagemUtils.messageFactoringFull(
                        MensagemUtils.getResourceBundle(v.getMessage(),
                        FacesContext.getCurrentInstance()), null,
                        FacesMessage.SEVERITY_ERROR,
                        FacesContext.getCurrentInstance());
                return MANTEM;
            } else {
                MensagemUtils.messageFactoringFull(v.getMessage(), null,
                        FacesMessage.SEVERITY_ERROR,
                        FacesContext.getCurrentInstance());
                return MANTEM;
            }
        }
    }

    @Override
    protected void performDestroy() {
        try {
            this.receitaDividaBean.apagarReceitaDivida(current,
                    apagarPrestacoes);
            MensagemUtils.messageFactoringFull("contaApagadaOK",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            clean();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                MensagemUtils.messageFactoringFull(
                        MensagemUtils.getResourceBundle(v.getMessage(),
                        FacesContext.getCurrentInstance()), null,
                        FacesMessage.SEVERITY_ERROR,
                        FacesContext.getCurrentInstance());
            } else {
                MensagemUtils.messageFactoringFull(v.getMessage(), null,
                        FacesMessage.SEVERITY_ERROR,
                        FacesContext.getCurrentInstance());
            }
        }
    }

    @Override
    protected ReceitaDivida getNewEntity() {
        ReceitaDivida rd = new ReceitaDivida();
        rd.setTipoMovimentacao(TipoMovimentacao.RETIRADA);
        return rd;
    }

    @Override
    protected void setEntity(ReceitaDivida t) {
        this.current = t;
    }

    @Override
    protected String update() {
        try {
            getFacade().salvarReceitaDivida(current);
            MensagemUtils.messageFactoringFull("contaPagarUpdated",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            return clean();
        } catch (ValidacaoException e) {
            MensagemUtils.messageFactoringFull(e.getMessage(),
                    e.getVariacoes(),
                    FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            return MANTEM;
        } catch (Exception e) {
            MensagemUtils.messageFactoringFull(e.getMessage(), null,
                    FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            return MANTEM;
        }
    }

    @Override
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(5) {

                @Override
                public int getItemsCount() {
                    return getFacade().buscarQutdadeReceitaDividasPorUsuarioStatusPaginada(
                            loginManager.getUsuario(),
                            StatusPagamento.NAO_PAGA,
                            TipoMovimentacao.RETIRADA, dataVencimento,
                            detalhe);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarReceitaDividasPorUsuarioStatusPaginada(
                            getPageFirstItem(), getPageSize(),
                            loginManager.getUsuario(),
                            StatusPagamento.NAO_PAGA,
                            TipoMovimentacao.RETIRADA, dataVencimento,
                            detalhe));
                }
            };
        }
        return pagination;
    }
    //====================
    //Métodos em Tabelas
    //====================

    //====================
    //Select Itens
    //====================
    public List<SelectItem> getDetalhes() {
        return selectItemManager.getDetalhesUsuario(loginManager.getUsuario(),
                true, TipoMovimentacao.RETIRADA);
    }

    //====================
    //Getters AND Setters
    //====================
    public ReceitaDividaBeanLocal getFacade() {
        return receitaDividaBean;
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

    /**
     * Recupera o Filtro de Data vencimento na paginação.
     */
    public Date getDataVencimento() {
        return dataVencimento;
    }
    /**
     * Define o filtro de Data vencimento na paginação.
     */
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    /**
     * Recupera o Filtro de detalhe pagamento na paginação.
     * @return 
     */
    public String getDetalhe() {
        return detalhe;
    }
    /**
     * Define o filtro de detalhe pagamento na paginação.
     */
    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }
    
}
