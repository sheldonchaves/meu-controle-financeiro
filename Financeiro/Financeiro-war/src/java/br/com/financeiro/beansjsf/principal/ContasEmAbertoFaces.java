/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.principal;

import br.com.financeiro.auxjsf.charts.ContasEmAbertoChart;
import br.com.financeiro.auxjsf.charts.factoring.ChartStore;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.cadastro.contas.PagamentoContaFaces;
import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.ejbbeans.interfaces.MovimentacaoFinanceiraLocal;
import br.com.financeiro.ejbbeans.interfaces.RelatoriosLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.MovimentacaoFinanceira;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import br.com.financeiro.utils.UtilMetodos;
import java.text.ParseException;
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
public class ContasEmAbertoFaces implements Observer {

    @EJB
    private ContaBancariaLocal contaBancariaBean;

    @EJB
    private MovimentacaoFinanceiraLocal movimentacaoFinanceiraBean;

    @EJB
    private RelatoriosLocal relatoriosBean;

    private User proprietario;

    private DataModel dataModel;

    private Locale locale = new Locale("pt", "BR");

    private Date initBusca;

    private int limiteContas = 10;

    /** Creates a new instance of ContasEmAberto */
    public ContasEmAbertoFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        this.initBusca = c.getTime();
    }

    public String getGraficoPizzaNPagoPago() throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(initBusca);
        ChartStore cs = new ContasEmAbertoChart(FacesContext.getCurrentInstance(), proprietario, locale);
        return cs.getCaminhoChar(new Object[]{initBusca, this.relatoriosBean.buscarNPagoPago(proprietario, c)});
    }

    public void atualizaPagamento(ActionEvent event) {
        Conta conta = (Conta) this.dataModel.getRowData();
        if (conta.getContaMovimentacaoFinanceira().getContaBancaria() == null) {
            UtilMetodos.messageFactoringFull("contaMovimentacaoNaoInformada", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            return;
        }
        try {
            this.movimentacaoFinanceiraBean.criarMovimentacaoFinanceira(conta);
            ControleObserver.notificaObservers(proprietario);
            UtilMetodos.messageFactoringFull("movimentacaoCriada", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } catch (MovimentacaoFinanceiraException me) {
            FacesMessage msg = new FacesMessage(me.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "MovimentacaoFinanceiraException");
        }
    }

    public List<SelectItem> getContaBancariasItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (ContaBancaria conta : this.contaBancariaBean.contasProprietario(proprietario)) {
            toReturn.add(new SelectItem(conta, conta.getLabel()));
        }
        return toReturn;
    }

    public void atualizaModelContas(ActionEvent event) {
        List<Conta> contasNaoVencidas = this.movimentacaoFinanceiraBean.buscarContasNaoVencidas(proprietario, initBusca, limiteContas);
        for (Conta conta : contasNaoVencidas) {
            conta.setContaMovimentacaoFinanceira(new MovimentacaoFinanceira());
        }
        this.dataModel = new ListDataModel(contasNaoVencidas);
    }

    public DataModel getDataModel() {
        if (this.dataModel == null) {
            atualizaModelContas(null);
        }
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Date getInitBusca() {
        return initBusca;
    }

    public void setInitBusca(Date initBusca) {
        this.initBusca = initBusca;
    }

    public int getLimiteContas() {
        return limiteContas;
    }

    public void setLimiteContas(int limiteContas) {
        this.limiteContas = limiteContas;
    }

    public void update(Observable o, Object arg) {
        atualizaModelContas(null);
    }
}
