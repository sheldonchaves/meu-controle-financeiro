/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.pages.movimentacao;

import br.com.gbvbahia.financeiro.beans.facades.CartaoCreditoFacade;
import br.com.gbvbahia.financeiro.beans.facades.ProcedimentoFacade;
import br.com.gbvbahia.financeiro.beans.facades.UsuarioFacade;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaProcedimento;
import br.com.gbvbahia.financeiro.modelos.commons.EntityInterface;
import br.com.gbvbahia.financeiro.modelos.dto.MinMaxDateDTO;
import br.com.gbvbahia.financeiro.utils.DateUtils;
import br.com.gbvbahia.projeto.web.constante.Meses;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Usuário do Windows
 */
@ManagedBean
@ViewScoped
public class CartaoOperacaoController implements Serializable {

    @EJB
    private ProcedimentoFacade procedimentoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private CartaoCreditoFacade cartaoFacade;
    //Tabela
    private List<DespesaProcedimento> despesas;
    //Filtros
    private CartaoCredito cartaoOperacao;
    private Meses mesOperacao;
    private Integer anoOperacao;
    private boolean todosFiltro;
    //SelectItem
    private List<Integer> listAnosSelect = new ArrayList<Integer>();
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
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, anoOperacao);
        c.set(Calendar.MONTH, mesOperacao.getMes());
        final Date[] intervalo = DateUtils.getIntervalo(c.getTime());
        despesas = procedimentoFacade.buscarDespesaIntervalo(usuarioFacade.getUsuario(),
                cartaoOperacao, StatusPagamento.NAO_PAGA, intervalo);
        if (despesas.isEmpty()) {
            MensagemUtils.messageFactoringFull("CartaoSemComprasPeriodo",
                    new String[]{DateUtils.getDateToString(intervalo[0]),
                        DateUtils.getDateToString(intervalo[1]),
                        cartaoOperacao.getLabel()}, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
        } else {
            for (DespesaProcedimento dp : despesas) {
                dp.setMarcadoTransient(true);
            }
            todosFiltro = true;
        }
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
//====================
// Select Itens
//====================

    public SelectItem[] getCartoes() {
        return JsfUtil.getSelectItems(new TreeSet<EntityInterface>(this.cartaoFacade.buscarCartoesAtivos(usuarioFacade.getUsuario())),
                true, FacesContext.getCurrentInstance());
    }

    public SelectItem[] getMeses() {
        return JsfUtil.getEnumSelectItems(Meses.class, true, FacesContext.getCurrentInstance());
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
}
