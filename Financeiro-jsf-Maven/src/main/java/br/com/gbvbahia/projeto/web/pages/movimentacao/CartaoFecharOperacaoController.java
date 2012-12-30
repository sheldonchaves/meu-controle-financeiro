/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.movimentacao;

import br.com.gbvbahia.financeiro.beans.business.interfaces.TrabalharOperacaoBusiness;
import br.com.gbvbahia.financeiro.beans.exceptions.NegocioException;
import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ContaBancariaFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.ContaBancaria;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.projeto.logger.I18nLogger;
import br.com.gbvbahia.projeto.web.constante.Meses;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import br.com.gbvbahia.projeto.web.pages.report.DisponivelReport;
import br.com.gbvbahia.projeto.web.pages.report.utils.ClassificacaoMakePie;
import br.com.gbvbahia.projeto.web.pages.report.utils.DetalheMakePie;
import br.com.gbvbahia.utils.MensagemUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class CartaoFecharOperacaoController implements Serializable {

    /**
     * Registra os eventos para debug em desenvolvimento.
     */
    private Logger logger = Logger.getLogger(CartaoFecharOperacaoController.class);
    @EJB
    private TrabalharOperacaoBusiness operacaoBusiness;
    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private CartaoCreditoFacade cartaoFacade;
    @EJB
    private ContaBancariaFacade disponivelFacade;
    @ManagedProperty("#{disponivelReport}")
    private DisponivelReport disponivelReport;
    //Tabela
    private List<DespesaProcedimento> despesas;
    //Filtros
    private CartaoCredito cartaoOperacao;
    private Meses mesOperacao;
    private Integer anoOperacao;
    private boolean todosFiltro;
    //SelectItem
    private List<Integer> listAnosSelect = new ArrayList<Integer>();
    //Conta Debito
    private ContaBancaria disponivel;
    private double total;
    //Gráfico
    private PieChartModel pieClassModel;
    private PieChartModel pieClassDetalhe;
    //Conta Editar
    private Procedimento proToEdit;
    private boolean showDialog = false;
//====================
// Acoes
//====================    

    /**
     *
     */
    public void buscarDespesas() {
        todosFiltro = false;
        if (cartaoOperacao == null
                || mesOperacao == null
                || anoOperacao == null) {
            MensagemUtils.messageFactoringFull("TodosCamposDevemSelecionados",
                    null, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
            return;
        }
        Date[] intervalo = despesasSearch();
        if (despesas.isEmpty()) {
            MensagemUtils.messageFactoringFull("CartaoSemComprasPeriodo",
                    new String[]{DateUtils.getDateToString(intervalo[0]),
                        DateUtils.getDateToString(intervalo[1]),
                        cartaoOperacao.getLabel()}, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
        }
    }

    private Date[] despesasSearch() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.YEAR, anoOperacao);
        c.set(Calendar.MONTH, mesOperacao.getMes());
        final Date[] intervalo = DateUtils.getIntervalo(c.getTime());
        despesas = procedimentoFacade.buscarDespesaIntervalo(usuarioFacade.getUsuario(),
                cartaoOperacao, StatusPagamento.NAO_PAGA, intervalo);
        total = 0;
        for (DespesaProcedimento dp : despesas) {
            dp.setMarcadoTransient(true);
            total += dp.getValor().doubleValue();
        }
        todosFiltro = true;
        return intervalo;
    }

    /**
     *
     */
    public void dataListener() {
        MinMaxDateDTO intervalodDatas = procedimentoFacade.buscarIntervalodDatas(cartaoOperacao,
                StatusPagamento.NAO_PAGA, usuarioFacade.getUsuario());
        listAnosSelect = intervalodDatas.intervaloMinMaxAnos();
        if (listAnosSelect.isEmpty()) {
            MensagemUtils.messageFactoringFull("CartaoSemCompras",
                    null, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
            despesas = null;
        }
    }

    public void todosListener() {
        if (todosFiltro) {
            for (DespesaProcedimento dp : getDespesas()) {
                dp.setMarcadoTransient(true);
            }
        } else {
            for (DespesaProcedimento dp : getDespesas()) {
                dp.setMarcadoTransient(false);
            }
        }
    }

    /**
     * Efetiva o fechamento das contas selecionadas.
     */
    public void efetuarPagamento() {
        if (disponivel == null) {
            MensagemUtils.messageFactoringFull("formCartaoOperacaoTable:contaDeb", "CartaoDisponivelEmpty",
                    null, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
            return;
        }
        List<Procedimento> toClose = new ArrayList<Procedimento>();
        for (DespesaProcedimento dp : getDespesas()) {
            if (dp.isMarcadoTransient()) {
                toClose.add(dp);
            }
        }
        if (toClose.isEmpty()) {
            MensagemUtils.messageFactoringFull("CartaoSemMarcada",
                    null, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
        } else {
            try {
                operacaoBusiness.fecharOperacoes(toClose, disponivel);
                MensagemUtils.messageFactoringFull("OperacaoCartaoFechada",
                        new String[]{cartaoOperacao.getLabel(),
                            disponivel.getLabel()}, FacesMessage.SEVERITY_INFO,
                        FacesContext.getCurrentInstance());
                disponivelReport.atualizarContas();
                despesas = null;
                disponivel = null;
            } catch (NegocioException ex) {
                MensagemUtils.messageFactoringFull(ex.getMessage(),
                        ex.getVariacoes(), FacesMessage.SEVERITY_ERROR,
                        FacesContext.getCurrentInstance());
            }
        }
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
            buscarDespesas();
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
//====================
// Select Itens
//====================

    public SelectItem[] getCartoes() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.cartaoFacade.buscarCartoesAtivos(usuarioFacade.getUsuario())),
                true, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getMeses() {
        return JsfUtil.getEnumSelectItems(Meses.class, false, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getContas() {
        return JsfUtil.getSelectItems(disponivelFacade.findAll(usuarioFacade.getUsuario(),
                Boolean.TRUE), true, FacesContext.getCurrentInstance());
    }

    /**
     *
     * @return
     */
    public SelectItem[] getAnos() {
        SelectItem[] anos = new SelectItem[listAnosSelect.size()];
        for (int i = 0; i < anos.length; i++) {
            anos[i] = new SelectItem(listAnosSelect.get(i), listAnosSelect.get(i).toString());
        }
        return anos;
    }

    //===================
    //Getter and Setters
    //===================
    public CartaoCredito getCartaoOperacao() {
        return cartaoOperacao;
    }

    public void setCartaoOperacao(CartaoCredito cartaoOperacao) {
        this.cartaoOperacao = cartaoOperacao;
    }

    public Meses getMesOperacao() {
        return mesOperacao;
    }

    public void setMesOperacao(Meses mesOperacao) {
        this.mesOperacao = mesOperacao;
    }

    public Integer getAnoOperacao() {
        return anoOperacao;
    }

    public void setAnoOperacao(Integer anoOperacao) {
        this.anoOperacao = anoOperacao;
    }

    public boolean isTodosFiltro() {
        return todosFiltro;
    }

    public void setTodosFiltro(boolean todosFiltro) {
        this.todosFiltro = todosFiltro;
    }

    public List<DespesaProcedimento> getDespesas() {
        if (despesas == null) {
            despesas = new ArrayList<DespesaProcedimento>();
        }
        return despesas;
    }

    public void setDespesas(List<DespesaProcedimento> despesas) {
        this.despesas = despesas;
    }

    public List<Integer> getListAnosSelect() {
        if (listAnosSelect == null) {
            listAnosSelect = new ArrayList<Integer>();
        }
        return listAnosSelect;
    }

    public ContaBancaria getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(ContaBancaria disponivel) {
        this.disponivel = disponivel;
    }

    public DisponivelReport getDisponivelReport() {
        return disponivelReport;
    }

    public void setDisponivelReport(DisponivelReport disponivelReport) {
        this.disponivelReport = disponivelReport;
    }

    public double getTotal() {
        return total;
    }

    public PieChartModel getPieClassModel() {
        pieClassModel = new ClassificacaoMakePie(new ArrayList<Procedimento>(getDespesas()), new PieChartModel(), FacesContext.getCurrentInstance()).makePie();
        return pieClassModel;
    }

    public PieChartModel getPieClassDetalhe() {
        pieClassDetalhe = new DetalheMakePie(new ArrayList<Procedimento>(getDespesas()), new PieChartModel(), FacesContext.getCurrentInstance()).makePie();
        return pieClassDetalhe;
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
