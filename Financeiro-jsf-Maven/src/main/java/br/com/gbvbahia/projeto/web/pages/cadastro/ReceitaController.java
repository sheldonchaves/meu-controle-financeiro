/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.cadastro;

import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.ClassificacaoProcedimento;
import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
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
public class ReceitaController extends EntityController<Procedimento>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(ReceitaController.class);
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private DetalheProcedimentoFacade detalheFacade;
    private Procedimento current;
    //Filtros
    private StatusPagamento statusFiltro = StatusPagamento.NAO_PAGA;
    private String observacaoFiltro;
    private Date dataFiltro;

    /**
     * Padrão
     */
    public ReceitaController() {
    }
    //====================
    //Iniciadores
    //====================

    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...ReceitaController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...ReceitaController");
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
                    return getFacade().contarProcedimentos(usuarioFacade.getUsuario(),
                            DetalheTipoProcedimento.RECEITA_UNICA, statusFiltro, observacaoFiltro, dataFiltro).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarProcedimentos(usuarioFacade.getUsuario(),
                            DetalheTipoProcedimento.RECEITA_UNICA, statusFiltro, observacaoFiltro, dataFiltro,
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
            MensagemUtils.messageFactoringFull("ProcedimentoCreated",
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
            MensagemUtils.messageFactoringFull("ProcedimentoUpdated",
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
            MensagemUtils.messageFactoringFull("ProcedimentoDeleted",
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
    public void setEntity(final Procedimento t) {
        this.current = t;
        if (this.current != null
                && this.current.getValorReal() == null) {
            this.current.setValorReal(current.getValorEstimado());
        }
    }

    @Override
    protected Procedimento getNewEntity() {
        Procedimento det = new Procedimento();
        det.setClassificacaoProcedimento(ClassificacaoProcedimento.VARIAVEL);
        det.setStatusPagamento(StatusPagamento.NAO_PAGA);
        det.setUsuario(usuarioFacade.getUsuario());
        det.setValorEstimado(BigDecimal.ZERO);
        return det;
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
                Boolean.TRUE, TipoProcedimento.RECEITA_FINANCEIRA)), true, FacesContext.getCurrentInstance());
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

    public Procedimento getCurrent() {
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
}
