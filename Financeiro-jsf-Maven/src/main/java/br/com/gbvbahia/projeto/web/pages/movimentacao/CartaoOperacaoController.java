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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
    private DataModel<DespesaProcedimento> despesas;
    //Filtros
    private CartaoCredito cartaoOperacao;
    private Meses mesOperacao;
    private Integer anoOperacao;
    //SelectItem
    private List<Integer> listAnos = new ArrayList<Integer>();
//====================
// Acoes
//====================    

    /**
     *
     */
    public void buscarDespesas() {
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
        List<DespesaProcedimento> desList =
                procedimentoFacade.buscarDespesaIntervalo(usuarioFacade.getUsuario(),
                cartaoOperacao, StatusPagamento.NAO_PAGA, intervalo);
        despesas = new ListDataModel<DespesaProcedimento>(desList);
        if (desList.isEmpty()) {
            MensagemUtils.messageFactoringFull("CartaoSemComprasPeriodo",
                    new String[]{DateUtils.getDateToString(intervalo[0]),
                        DateUtils.getDateToString(intervalo[1]),
                        cartaoOperacao.getLabel()}, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
        }
    }

    /**
     *
     */
    public void dataListener() {
        MinMaxDateDTO intervalodDatas = procedimentoFacade.buscarIntervalodDatas(cartaoOperacao,
                StatusPagamento.NAO_PAGA, usuarioFacade.getUsuario());
        listAnos = intervalodDatas.intervaloMinMaxAnos();
        if (listAnos.isEmpty()) {
            MensagemUtils.messageFactoringFull("CartaoSemCompras",
                    null, FacesMessage.SEVERITY_WARN,
                    FacesContext.getCurrentInstance());
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
        SelectItem[] anos = new SelectItem[listAnos.size()];
        for (int i = 0; i < anos.length; i++) {
            anos[i] = new SelectItem(listAnos.get(i), listAnos.get(i).toString());
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

    public DataModel<DespesaProcedimento> getDespesas() {
        return despesas;
    }

    public void setDespesas(DataModel<DespesaProcedimento> despesas) {
        this.despesas = despesas;
    }
}
