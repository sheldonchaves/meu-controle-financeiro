/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.movimentacao;

import br.com.gbvbahia.financeiro.beans.business.interfaces.TrabalharOperacaoBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.projeto.web.pages.report.DisponivelReport;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.util.Date;
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
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class OperacaoController extends EntityController<Procedimento> implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(OperacaoController.class);
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ContaBancariaFacade disponivelFacade;
    @EJB
    private TrabalharOperacaoBusiness operacaoBusiness;
    @ManagedProperty("#{disponivelReport}")
    private DisponivelReport disponivelReport;
    private ContaBancaria disponivel;
    private Procedimento current;
    //Filtros
    private StatusPagamento statusFiltro = StatusPagamento.NAO_PAGA;
    private String observacaoFiltro;
    private DetalheTipoProcedimento detalheFiltro;
    private Date dataFiltro;
    //Conta Editar
    private Procedimento proToEdit;
    private boolean showDialog = false;

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...OperacaoController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...OperacaoController");
    }

    @Override
    protected void setEntity(Procedimento t) {
        this.current = t;
    }

    /**
     * Fecha a operação selecionada.
     *
     * @return
     */
    public String fecharOperacao() {
        try {
            setEntity(getItems().getRowData());
            if (disponivel == null) {
                MensagemUtils.messageFactoringFull("formOperacaoTable:contaDeb", "MovimentacaoProcedimentoSemConta",
                        new String[]{current.getObservacao()}, FacesMessage.SEVERITY_WARN,
                        FacesContext.getCurrentInstance());
                return JsfUtil.MANTEM;
            }
            operacaoBusiness.fecharOperacao(current, disponivel);
            MensagemUtils.messageFactoringFull("OperacaoFechada",
                    new String[]{current.getObservacao()}, FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            disponivelReport.atualizarContas();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("createError", current.toString()));
            return JsfUtil.MANTEM;
        }
    }

    /**
     * Abri uma operação fechada.
     *
     * @return
     */
    public String abrirOperacao() {
        try {
            setEntity(getItems().getRowData());
            operacaoBusiness.abrirOperacao(current);
            MensagemUtils.messageFactoringFull("OperacaoAberta",
                    new String[]{current.getObservacao()}, FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            disponivelReport.atualizarContas();
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
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {
                @Override
                public int getItemsCount() {
                    return getFacade().contarProcedimentosSemCartao(usuarioFacade.getUsuario(),
                            detalheFiltro, statusFiltro, observacaoFiltro, dataFiltro).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarProcedimentosSemCartao(usuarioFacade.getUsuario(),
                            detalheFiltro, statusFiltro, observacaoFiltro, dataFiltro,
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public void cleanDate() {
        this.dataFiltro = null;
        recreateTable();
    }

    public void alterarProcedimento() {
        try {
            if (proToEdit.getDataMovimentacao() == null) {
                MensagemUtils.messageFactoringFull("dataMovimentacaoNull",
                        null, FacesMessage.SEVERITY_WARN,
                        FacesContext.getCurrentInstance());
                return;
            } else if (proToEdit.getValorReal() == null) {
                MensagemUtils.messageFactoringFull("valorRealNull",
                        null, FacesMessage.SEVERITY_WARN,
                        FacesContext.getCurrentInstance());
                return;
            }
            procedimentoFacade.update(proToEdit);
            MensagemUtils.messageFactoringFull("OperacaoValorAlterado",
                    new Object[]{proToEdit.getObservacao()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            showDialog = false;
            proToEdit = null;
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            logger.info(I18nLogger.getMsg("createError", proToEdit.toString()));
        }
    }

    public void handleClose(CloseEvent event) {
        setShowDialog(false);
        this.proToEdit = null;
    }

    @Override
    protected Procedimento getNewEntity() {
        throw new UnsupportedOperationException("Funcionalidade não implementada.");
    }

    @Override
    protected void performDestroy() {
        throw new UnsupportedOperationException("Funcionalidade não implementada.");
    }

    @Override
    protected String create() {
        throw new UnsupportedOperationException("Funcionalidade não implementada.");
    }

    @Override
    protected String update() {
        throw new UnsupportedOperationException("Funcionalidade não implementada.");
    }
    //====================
    // Select Itens
    //====================

    public SelectItem[] getStatus() {
        return JsfUtil.getEnumSelectItems(StatusPagamento.class, false,
                FacesContext.getCurrentInstance());
    }

    public SelectItem[] getContas() {
        return JsfUtil.getSelectItems(disponivelFacade.findAll(usuarioFacade.getUsuario(),
                Boolean.TRUE), true, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getDetalhesTipos() {
        return JsfUtil.getEnumSelectItems(DetalheTipoProcedimento.class, false,
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

    public DetalheTipoProcedimento getDetalheFiltro() {
        return detalheFiltro;
    }

    public void setDetalheFiltro(DetalheTipoProcedimento detalheFiltro) {
        this.detalheFiltro = detalheFiltro;
    }

    public Date getDataFiltro() {
        return dataFiltro;
    }

    public void setDataFiltro(Date dataFiltro) {
        this.dataFiltro = dataFiltro;
    }

    public DisponivelReport getDisponivelReport() {
        return disponivelReport;
    }

    public void setDisponivelReport(DisponivelReport disponivelReport) {
        this.disponivelReport = disponivelReport;
    }

    public ContaBancaria getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(ContaBancaria disponivel) {
        this.disponivel = disponivel;
    }

    public Procedimento getProToEdit() {
        return proToEdit;
    }

    public void setProToEdit(Procedimento proToEdit) {
        this.proToEdit = proToEdit;
        if (proToEdit == null) {
            setShowDialog(false);
        } else {
            setShowDialog(true);
        }
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
