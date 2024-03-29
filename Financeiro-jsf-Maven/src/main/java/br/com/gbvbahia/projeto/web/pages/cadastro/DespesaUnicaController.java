/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.cadastro;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.TreeSet;
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
 *
 * @author Guilherme
 */
@ManagedBean
@ViewScoped
public class DespesaUnicaController extends EntityController<DespesaProcedimento>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(DespesaUnicaController.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private CartaoCreditoFacade cartaoFacade;
    @EJB
    private DetalheProcedimentoFacade detalheFacade;
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    private DespesaProcedimento current;
    //Filtros
    private StatusPagamento statusFiltro = StatusPagamento.NAO_PAGA;
    private String observacaoFiltro;
    private Date dataFiltro;
    private CartaoCredito cartaoFiltro;

    /**
     * Padrão.
     */
    public DespesaUnicaController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...DespesaUnicaController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...DespesaUnicaController");
    }

    //====================
    //Métodos Sobrescritos
    //====================
    @Override
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {
                @Override
                public int getItemsCount() {
                    return getFacade().contarDespesas(usuarioFacade.getUsuario(),
                            DetalheTipoProcedimento.DESPESA_UNICA, statusFiltro, observacaoFiltro,
                            dataFiltro, cartaoFiltro).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarDespesas(usuarioFacade.getUsuario(),
                            DetalheTipoProcedimento.DESPESA_UNICA, statusFiltro,
                            observacaoFiltro, dataFiltro, cartaoFiltro,
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    @Override
    public String clean() {
        super.clean();
        return JsfUtil.MANTEM;
    }

    @Override
    protected String create() {
        try {
            getFacade().create(current);
            MensagemUtils.messageFactoringFull("DespesaCreated",
                    new Object[]{current.getObservacao()},
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
            MensagemUtils.messageFactoringFull("DespesaUpdated",
                    new Object[]{current.getObservacao()},
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
            MensagemUtils.messageFactoringFull("DespesaDeleted",
                    new Object[]{current.getObservacao()},
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
    public void setEntity(final DespesaProcedimento t) {
        this.current = t;
        if (this.current != null
                && this.current.getValorReal() == null) {
            this.current.setValorReal(current.getValorEstimado());
        }
    }

    @Override
    protected DespesaProcedimento getNewEntity() {
        DespesaProcedimento det = new DespesaProcedimento();
        det.setClassificacaoProcedimento(ClassificacaoProcedimento.VARIAVEL);
        det.setStatusPagamento(StatusPagamento.NAO_PAGA);
        det.setUsuario(usuarioFacade.getUsuario());
        det.setValorEstimado(BigDecimal.ZERO);
        return det;
    }

    public void dataListener() {
        if (current != null && current.getCartaoCredito() != null) {
            current.setDataCartao(current.getCartaoCredito().getProximoVencimento(current.getDataMovimentacao()));
        }
    }

    public void cleanDate() {
        this.dataFiltro = null;
        recreateTable();
    }
    //====================
    // Select Itens
    //====================

    public SelectItem[] getStatus() {
        return JsfUtil.getEnumSelectItems(StatusPagamento.class, false,
                FacesContext.getCurrentInstance());
    }

    public SelectItem[] getDetalhes() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.detalheFacade.findAllDetalhe(usuarioFacade.getUsuario(),
                Boolean.TRUE, TipoProcedimento.DESPESA_FINANCEIRA)), true, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getCartoes() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.cartaoFacade.buscarCartoesAtivos(usuarioFacade.getUsuario())),
                true, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getCartoesTabela() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.cartaoFacade.buscarCartoesAtivos(usuarioFacade.getUsuario())),
                false, FacesContext.getCurrentInstance());
    }
    //====================
    //Getters AND Setters
    //====================

    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public ProcedimentoFacade getFacade() {
        return procedimentoFacade;
    }

    public DespesaProcedimento getCurrent() {
        return current;
    }

    public StatusPagamento getStatusFiltro() {
        return statusFiltro;
    }

    public void setStatusFiltro(StatusPagamento statusFiltro) {
        this.statusFiltro = statusFiltro;
    }

    public String getObservacaoFiltro() {
        return observacaoFiltro;
    }

    public void setObservacaoFiltro(String observacaoFiltro) {
        this.observacaoFiltro = observacaoFiltro;
    }

    public Date getDataFiltro() {
        return dataFiltro;
    }

    public void setDataFiltro(Date dataFiltro) {
        this.dataFiltro = dataFiltro;
    }

    public CartaoCredito getCartaoFiltro() {
        return cartaoFiltro;
    }

    public void setCartaoFiltro(CartaoCredito cartaoFiltro) {
        this.cartaoFiltro = cartaoFiltro;
    }
}
