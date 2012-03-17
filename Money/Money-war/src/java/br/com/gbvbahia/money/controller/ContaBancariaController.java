package br.com.gbvbahia.money.controller;

import br.com.gbvbahia.money.controller.util.JsfUtil;
import br.com.gbvbahia.money.controller.util.PaginationHelper;
import br.com.gbvbahia.money.manager.LoginManager;
import br.com.gbvbahia.money.manager.SelectItemManager;
import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.enums.TipoConta;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import java.io.Serializable;
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

@ManagedBean(name = "contaBancariaController")
@ViewScoped
public class ContaBancariaController extends EntityController<ContaBancaria>
        implements Serializable {

    public static final String CONTA_BANCARIA_CHANGE = "contasbancarias";
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    private ContaBancaria current;
    /**
     * Utilizado como filtro na paginação com pagination herdade de
     * EntityController.
     */
    private String nome;

    public ContaBancariaController() {
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
    public void setEntity(ContaBancaria current) {
        this.current = current;
    }

    @Override
    public String create() {
        try {
            getFacade().salvarContaBancaria(current);
            MensagemUtils.messageFactoringFull("contaBancariaCadastradoOk",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(),
                    ControleObserver.Eventos.CAD_CONTA_BANCARIA);
            recreateTable();
            return clean();
        } catch (ValidacaoException v) {
            MensagemUtils.messageFactoringFull(v.getMessage(),
                    v.getVariacoes(), FacesMessage.SEVERITY_ERROR,
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
    protected ContaBancaria getNewEntity() {
        ContaBancaria cb = new ContaBancaria();
        cb.setUser(loginManager.getUsuario());
        return cb;
    }

    @Override
    protected String update() {
        try {
            getFacade().salvarContaBancaria(current);
            MensagemUtils.messageFactoringFull("contaBancariaEditadaOk",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(),
                    ControleObserver.Eventos.CAD_CONTA_BANCARIA);
            recreateTable();
            return clean();
        } catch (ValidacaoException v) {
            MensagemUtils.messageFactoringFull(v.getMessage(),
                    v.getVariacoes(), FacesMessage.SEVERITY_ERROR,
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
    protected void performDestroy() {
        try {
            getFacade().apagarContaBancaria(current.getId());
            MensagemUtils.messageFactoringFull("contaBancariaDeleteOk",
                    new Object[]{current.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            clean();
            ControleObserver.notificaObservers(loginManager.getUsuario(),
                    ControleObserver.Eventos.CAD_CONTA_BANCARIA);
        } catch (ValidacaoException v) {
            MensagemUtils.messageFactoringFull(v.getMessage(),
                    v.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
        } catch (Exception e) {
            MensagemUtils.messageFactoringFull(e.getMessage(), null,
                    FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
        }
    }

    @Override
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper() {

                @Override
                public int getItemsCount() {
                    return getFacade().contarContaBancariasPorUsuarioTipo(
                            loginManager.getUsuario(), null, nome);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(
                    getFacade().buscarContaBancariasPorUsuarioTipoPaginado(
                    loginManager.getUsuario(), null, nome,
                    getPageFirstItem(),
                    getPageFirstItem() + getPageSize()));
                }
            };
        }
        return pagination;
    }
    //====================
    //Métodos Filtros Paginação
    //====================

    /**
     * Retorna o nome utilizado para filtro na paginação.
     *
     * @return nome java.lang.String
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do Órgão para filtrar na paginação.
     *
     * @param nomeParam java.lang.String
     */
    public void setNome(final String nomeParam) {
        this.nome = nomeParam;
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

    public ContaBancariaBeanLocal getFacade() {
        return contaBancariaBean;
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

    public SelectItemManager getSelectItemManager() {
        return selectItemManager;
    }

    public void setSelectItemManager(SelectItemManager selectItemManager) {
        this.selectItemManager = selectItemManager;
    }
}
