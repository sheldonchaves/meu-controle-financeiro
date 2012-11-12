/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.cadastro;

import br.com.gbvbahia.financeiro.beans.business.interfaces.ProvisaoFacade;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.AgendaProcedimentoFixoFacade;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.DetalheProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.Periodo;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.AgendaProcedimentoFixo;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
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
 * @author Guilherme Braga
 */
@ManagedBean
@ViewScoped
public class AgendaController extends EntityController<AgendaProcedimentoFixo>
        implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(AgendaProcedimentoFixo.class);
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private DetalheProcedimentoFacade detalheFacade;
    @EJB
    private AgendaProcedimentoFixoFacade agendaFacade;
    @EJB
    private ProvisaoFacade provisaoFacade;
    @EJB
    private CartaoCreditoFacade cartaoFacade;
    
    private AgendaProcedimentoFixo current;
    private TipoProcedimento tipo;
    //Filtros Tabela
    private TipoProcedimento tipoFiltro;
    private String observacaoFiltro;

    /**
     * Padrão
     */
    public AgendaController() {
    }

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...AgendaController");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...AgendaController");
    }

    //====================
    //Métodos Sobrescritos
    //====================
    @Override
    public String clean() {
        super.clean();
        this.tipo = null;
        return JsfUtil.MANTEM;
    }

    @Override
    public EntityPagination getPagination() {
        if (pagination == null) {
            pagination = new EntityPagination() {
                @Override
                public int getItemsCount() {
                    return getFacade().countarAgendaPorUserDetalheObservacaoTipo(usuarioFacade.getUsuario(), null, observacaoFiltro, tipoFiltro).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().
                            buscarAgendaPorUserDetalheObservacaoTipoPaginado(usuarioFacade.getUsuario(),
                            null, observacaoFiltro, tipoFiltro,
                            new int[]{getPageFirstItem(), getPageFirstItem()
                                + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    @Override
    protected String create() {
        try {
            provisaoFacade.criarAgendaEProvisionar(current);
            MensagemUtils.messageFactoringFull("AgendaCreated",
                    new Object[]{current.getLabel()},
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
            provisaoFacade.atualizarProvisao(current);
            MensagemUtils.messageFactoringFull("AgendaUpdated",
                    new Object[]{current.getLabel()},
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
            provisaoFacade.alterarStatusProvisao(current);
            MensagemUtils.messageFactoringFull(current.isAtiva() ? "AgendaAtivada" : "AgendaDestivada",
                    new Object[]{current.getLabel()},
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
    public void setEntity(final AgendaProcedimentoFixo t) {
        this.current = t;
        if (current != null && current.getDetalhe() != null) {
            this.tipo = current.getDetalhe().getTipo();
        }
    }

    @Override
    protected AgendaProcedimentoFixo getNewEntity() {
        AgendaProcedimentoFixo det = new AgendaProcedimentoFixo();
        det.setUsuario(usuarioFacade.getUsuario());
        return det;
    }

    public void dataListener() {
        if (current != null && current.getCartaoCredito() != null) {
            current.setDataPrimeiroVencimento(current.getCartaoCredito().getProximoVencimento());
        }
    }
    //====================
    //Métodos Filtros Paginação
    //====================
    //====================
    // Select Itens
    //====================

    public SelectItem[] getTipos() {
        return JsfUtil.getEnumSelectItems(TipoProcedimento.class, false,
                FacesContext.getCurrentInstance());
    }

    public SelectItem[] getDetalhes() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.detalheFacade.findAllDetalhe(usuarioFacade.getUsuario(),
                Boolean.TRUE, tipo)), true, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getPeriodos() {
        return JsfUtil.getEnumSelectItems(Periodo.class, false,
                FacesContext.getCurrentInstance());
    }

    public SelectItem[] getCartoes() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.cartaoFacade.buscarCartoesAtivos(usuarioFacade.getUsuario())),
                true, FacesContext.getCurrentInstance());
    }
    //====================
    //Getters AND Setters
    //====================

    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public AgendaProcedimentoFixoFacade getFacade() {
        return agendaFacade;
    }

    /**
     * Usuário atual, pode ser null, pode ser um novo ou um já existente.
     *
     * @return Usuario
     */
    public AgendaProcedimentoFixo getCurrent() {
        return current;
    }

    public TipoProcedimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoProcedimento tipo) {
        this.tipo = tipo;
    }

    public TipoProcedimento getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(TipoProcedimento tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public String getObservacaoFiltro() {
        return observacaoFiltro;
    }

    public void setObservacaoFiltro(String observacaoFiltro) {
        this.observacaoFiltro = observacaoFiltro;
    }
}
