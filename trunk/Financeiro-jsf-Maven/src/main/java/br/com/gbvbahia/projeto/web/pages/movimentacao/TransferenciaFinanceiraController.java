/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.movimentacao;

import br.com.gbvbahia.financeiro.beans.business.interfaces.TrabalharTransferenciaBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.MovimentacaoFinanceiraFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.MovimentacaoTrasnferencia;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.common.EntityController;
import br.com.gbvbahia.projeto.web.common.EntityPagination;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.projeto.web.pages.report.DisponivelReport;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Guilherme
 */
@ManagedBean
@ViewScoped
public class TransferenciaFinanceiraController extends EntityController<MovimentacaoTrasnferencia> implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(TransferenciaFinanceiraController.class);
    //EJBS
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ContaBancariaFacade disponivelFacade;
    @EJB
    private MovimentacaoFinanceiraFacade movimentacaoFacade;
    @EJB
    private TrabalharTransferenciaBusiness movimentacaoBusiness;
    //Beans JSF Injetados
    @ManagedProperty("#{disponivelReport}")
    private DisponivelReport disponivelReport;
    //Variaveis
    private MovimentacaoTrasnferencia current;
    private ContaBancaria creditada;
    private ContaBancaria debitada;
    private BigDecimal valor;
    //Filtro
    private Date dataFiltro;
    private ContaBancaria debitadaFiltro;
    private ContaBancaria creditadaFiltro;

    //====================
    //Iniciadores
    //====================
    /**
     * Executado após o bean JSF ser criado.
     */
    @PostConstruct
    public void init() {
        logger.info("init()...TransferenciaFinanceira");
    }

    /**
     * Executado antes do bean JSF ser destruído.
     */
    @PreDestroy
    public void end() {
        logger.info("end()...TransferenciaFinanceira");
    }

    @Override
    protected void setEntity(MovimentacaoTrasnferencia t) {
        this.current = t;
    }

    @Override
    protected MovimentacaoTrasnferencia getNewEntity() {
        return null;
    }

    @Override
    protected void performDestroy() {
        try {
            movimentacaoBusiness.desfazerTransferencia(current);
            MensagemUtils.messageFactoringFull("TransferenciaCanceladaOk",
                    new String[]{current.getLabel()}, FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            disponivelReport.atualizarContas();
            clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
        }
    }

    @Override
    public String create() {
        try {
            movimentacaoBusiness.transferirEntreDisponiveis(debitada, creditada, valor);
            MensagemUtils.messageFactoringFull("TransferenciaFeitaOk",
                    new Object[]{valor.toString(), debitada.getLabel(),
                        creditada.getLabel()},
                    FacesMessage.SEVERITY_INFO,
                    FacesContext.getCurrentInstance());
            recreateTable();
            disponivelReport.atualizarContas();
            return clean();
        } catch (NegocioException ex) {
            MensagemUtils.messageFactoringFull(ex.getMessage(),
                    ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                    FacesContext.getCurrentInstance());
            return JsfUtil.MANTEM;
        }
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
                    return getFacade().contarTransferencias(usuarioFacade.getUsuario(),
                            dataFiltro, debitadaFiltro, creditadaFiltro).intValue();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().buscarTransferencias(usuarioFacade.getUsuario(),
                            dataFiltro, debitadaFiltro, creditadaFiltro,
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

    @Override
    public String clean() {
        this.creditada = null;
        this.debitada = null;
        this.valor = null;
        return super.clean();
    }
    
    
    //====================
    // Select Itens
    //====================

    public SelectItem[] getContas() {
        return JsfUtil.getSelectItems(disponivelFacade.findAll(usuarioFacade.getUsuario(),
                Boolean.TRUE), true, FacesContext.getCurrentInstance());
    }

    //====================
    //Getters AND Setters
    //====================
    /**
     * O Facade que representa a entidade current.
     *
     * @return AbstractEntityBeans
     */
    public MovimentacaoFinanceiraFacade getFacade() {
        return movimentacaoFacade;
    }

    public MovimentacaoTrasnferencia getCurrent() {
        return current;
    }

    public DisponivelReport getDisponivelReport() {
        return disponivelReport;
    }

    public void setDisponivelReport(DisponivelReport disponivelReport) {
        this.disponivelReport = disponivelReport;
    }

    public ContaBancaria getCreditada() {
        return creditada;
    }

    public void setCreditada(ContaBancaria creditada) {
        this.creditada = creditada;
    }

    public ContaBancaria getDebitada() {
        return debitada;
    }

    public void setDebitada(ContaBancaria debitada) {
        this.debitada = debitada;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDataFiltro() {
        return dataFiltro;
    }

    public void setDataFiltro(Date dataFiltro) {
        this.dataFiltro = dataFiltro;
    }

    public ContaBancaria getDebitadaFiltro() {
        return debitadaFiltro;
    }

    public void setDebitadaFiltro(ContaBancaria debitadaFiltro) {
        this.debitadaFiltro = debitadaFiltro;
    }

    public ContaBancaria getCreditadaFiltro() {
        return creditadaFiltro;
    }

    public void setCreditadaFiltro(ContaBancaria creditadaFiltro) {
        this.creditadaFiltro = creditadaFiltro;
    }
}
