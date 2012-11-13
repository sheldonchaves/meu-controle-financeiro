/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.movimentacao;

import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.DetalheTipoProcedimento;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.event.DateSelectEvent;

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
    private Procedimento current;
    //Filtros
    private StatusPagamento statusFiltro = StatusPagamento.NAO_PAGA;
    private String observacaoFiltro;
    private DetalheTipoProcedimento detalheFiltro;
    private Date dataFiltro;

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

    @Override
    protected Procedimento getNewEntity() {
        throw new UnsupportedOperationException("Funcionalidade não implementada.");
    }

    @Override
    protected void performDestroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String update() {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
