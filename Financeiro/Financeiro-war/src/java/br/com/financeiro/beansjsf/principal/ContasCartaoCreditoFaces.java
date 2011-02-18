/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.principal;

import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.ejbbeans.interfaces.MovimentacaoFinanceiraLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.MovimentacaoFinanceira;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.StatusPagamento;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class ContasCartaoCreditoFaces implements Observer {

    @EJB
    private ContaBancariaLocal contaBancariaBean;

    @EJB
    private MovimentacaoFinanceiraLocal movimentacaoFinanceiraBean;

    @EJB
    private CartaoCreditoLocal cartaoCreditoBean;

    private CartaoCreditoUnico cartaoCreditoUnico;

    private ContaBancaria contaDebitar;

    private Date mesCartao;

    private double totalPagar;

    private double totalFatura;

    private User proprietario;

    private DataModel dataModelContas;

    private Locale locale;

    {
        Calendar c = Calendar.getInstance();
        mesCartao = c.getTime();
        locale = new Locale("pt", "BR");
    }

    /** Creates a new instance of ContasCartaoCreditoFaces */
    public ContasCartaoCreditoFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
    }

    public void atualizaPagamento(ActionEvent event) {
        List<ContaPagar> contas = (List<ContaPagar>) this.dataModelContas.getWrappedData();
        boolean problema = false;
        for (ContaPagar conta : contas) {
            if (conta.getStatusPagamento().equals(StatusPagamento.NAO_PAGA)) {
                conta.setContaMovimentacaoFinanceira(new MovimentacaoFinanceira());
                conta.getMovimentacaoFinanceira().setContaBancaria(contaDebitar);
                try {
                    this.movimentacaoFinanceiraBean.criarMovimentacaoFinanceira(conta);
                } catch (MovimentacaoFinanceiraException me) {
                    problema = true;
                    FacesMessage msg = new FacesMessage(me.getMessage() + " A conta: " + conta.getObservacao() + " de valor: " + UtilMetodos.currencyFormat(conta.getValor()) + " não foi paga.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    Logger.getLogger(ContasCartaoCreditoFaces.class.getName()).log(Level.INFO, "MovimentacaoFinanceiraException");
                }
            }
        }
        ControleObserver.notificaObservers(proprietario);
        if (!problema) {
            UtilMetodos.messageFactoringFull("movimentacaoCriada", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } else {
            UtilMetodos.messageFactoringFull("movimentacaoCriadaProblemas", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
        }
    }

    public List<SelectItem> getContaBancariasItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (ContaBancaria conta : this.contaBancariaBean.contasProprietario(proprietario)) {
            toReturn.add(new SelectItem(conta, conta.getLabel()));
        }
        return toReturn;
    }

    public List<SelectItem> getCartoesCredito() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        toReturn.add(new SelectItem("Selecione", "Selecione"));
        for (CartaoCreditoUnico cc : this.cartaoCreditoBean.buscaCartaoPorStatus(true, proprietario)) {
            toReturn.add(new SelectItem(cc, cc.getLabelCartao()));
        }
        return toReturn;
    }

    public void atualizarListaContasCartaoCredito(ActionEvent event) {
        List<ContaPagar> toReturn = this.cartaoCreditoBean.buscarContasPorCartaoCredito(cartaoCreditoUnico, mesCartao);
        this.totalPagar = 0.0;
        this.totalFatura = 0.0;
        for (ContaPagar cp : toReturn) {
            if (cp.getStatusPagamento().equals(StatusPagamento.NAO_PAGA)) {
                totalPagar += cp.getValor();
            }
            totalFatura += cp.getValor();
        }
        this.dataModelContas = new ListDataModel(toReturn);
    }

    public CartaoCreditoUnico getCartaoCreditoUnico() {
        return cartaoCreditoUnico;
    }

    public void setCartaoCreditoUnico(CartaoCreditoUnico cartaoCreditoUnico) {
        this.cartaoCreditoUnico = cartaoCreditoUnico;
    }

    public Date getMesCartao() {
        return mesCartao;
    }

    public void setMesCartao(Date mesCartao) {
        this.mesCartao = mesCartao;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public DataModel getDataModelContas() {
        return dataModelContas;
    }

    public void setDataModelContas(DataModel dataModelContas) {
        this.dataModelContas = dataModelContas;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public double getTotalFatura() {
        return totalFatura;
    }

    public void setTotalFatura(double totalFatura) {
        this.totalFatura = totalFatura;
    }

    public ContaBancaria getContaDebitar() {
        return contaDebitar;
    }

    public void setContaDebitar(ContaBancaria contaDebitar) {
        this.contaDebitar = contaDebitar;
    }

    public void update(Observable o, Object arg) {
        if (this.cartaoCreditoUnico != null && this.mesCartao != null) {
            this.atualizarListaContasCartaoCredito(null);
        }
    }
}
