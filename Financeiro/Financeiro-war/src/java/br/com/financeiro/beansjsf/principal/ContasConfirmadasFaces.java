/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.principal;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.cadastro.contas.PagamentoContaFaces;
import br.com.financeiro.ejbbeans.interfaces.MovimentacaoFinanceiraLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.interfaces.Conta;
import br.com.financeiro.excecoes.MovimentacaoFinanceiraException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.Calendar;
import java.util.Date;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class ContasConfirmadasFaces implements Observer{

    @EJB
    private MovimentacaoFinanceiraLocal movimentacaoFinanceiraBean;
    private Locale locale = new Locale("pt", "BR");
    private User proprietario;
    private DataModel contadataModel;
    private Date dateInit;
    private int maxRows = 10;

    /** Creates a new instance of ContasConfirmadasFaces */
    public ContasConfirmadasFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        this.dateInit = c.getTime();
    }

    public void removerMovimentacao(ActionEvent event) {
        Conta conta = (Conta) this.contadataModel.getRowData();
        try {
            this.movimentacaoFinanceiraBean.removerMovimentacaoFinanceira(conta);
            ControleObserver.notificaObservers(proprietario);
            UtilMetodos.messageFactoringFull("movimentacaoRemovida", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } catch (MovimentacaoFinanceiraException e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "MovimentacaoFinanceiraException");
        }
    }

    public void atualizaModelContas(ActionEvent event) {
        this.contadataModel = new ListDataModel(this.movimentacaoFinanceiraBean.buscarContasPagas(proprietario, dateInit, maxRows));
    }

    public DataModel getContadataModel() {
        if (this.contadataModel == null) {
            atualizaModelContas(null);
        }
        return contadataModel;
    }

    public void setContadataModel(DataModel contadataModel) {
        this.contadataModel = contadataModel;
    }

   public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public Date getDateInit() {
        return dateInit;
    }

    public void setDateInit(Date dateInit) {
        this.dateInit = dateInit;
    }

    public void update(Observable o, Object arg) {
        atualizaModelContas(null);
    }


}
