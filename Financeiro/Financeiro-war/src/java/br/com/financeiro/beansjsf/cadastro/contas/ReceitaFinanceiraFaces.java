/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.cadastro.contas;

import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.ejbbeans.interfaces.ReceitaFinanceiraLocal;
import br.com.financeiro.entidades.ContaReceber;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoReceita;
import br.com.financeiro.entidades.enums.FormaRecebimento;
import br.com.financeiro.entidades.enums.StatusReceita;
import br.com.financeiro.excecoes.ContaPagarReceberValueException;
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
public class ReceitaFinanceiraFaces implements Observer{

    @EJB
    private GrupoFinanceiroLocal grupoFinanceiroBean;
    @EJB
    private ReceitaFinanceiraLocal receitaFinanceiraBean;
    private Locale locale = new Locale("pt", "BR");
    private User proprietario;
    private boolean salvarParcelas;
    private boolean exibirCadReceita = false;
    private DataModel dataModel;
    private ContaReceber contaReceber;
    private Date initBusca;
    private int limiteContas = 10;
    /** Creates a new instance of ReceitaFinanceiraFaces */
    public ReceitaFinanceiraFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        this.initBusca = c.getTime();
        clean(null);
    }

       public void pegarReceita(ActionEvent event){
        this.contaReceber = (ContaReceber) this.dataModel.getRowData();
    }

    public void apagar(ActionEvent evnet) {
        try {
            this.receitaFinanceiraBean.apagarReceitaFinanceira(this.contaReceber);
            ControleObserver.notificaObservers(proprietario);
            clean(evnet);
            UtilMetodos.messageFactoringFull("contaReceberApagaga", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            this.exibirCadReceita = false;
        } catch (ContaPagarReceberValueException e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "ContaPagarReceberValueException");
        }
    }

    public void salvar(ActionEvent event) {
        try {
            this.receitaFinanceiraBean.salvarReceitaFinanceira(contaReceber);
            if (salvarParcelas) {
                ContaReceber receita = null;
                for (receita = contaReceber; (receita = aumentaParcela(receita)) != null;) {
                    this.receitaFinanceiraBean.salvarReceitaFinanceira(receita);
                }
            }
            UtilMetodos.messageFactoringFull("contaReceitaSalva", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            clean(event);
            ControleObserver.notificaObservers(proprietario);
            this.exibirCadReceita = false;
        } catch (ContaPagarReceberValueException e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "ContaPagarReceberValueException");
        }
    }

    public List<SelectItem> getFormaReceitaItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (FormaRecebimento fp : FormaRecebimento.values()) {
            toReturn.add(new SelectItem(fp, fp.toString()));
        }
        return toReturn;
    }

    public List<SelectItem> getGrupoReceitaItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (GrupoReceita gg : this.grupoFinanceiroBean.buscarGrupoReceitaParaSelecao(proprietario)) {
            toReturn.add(new SelectItem(gg, gg.getGrupoReceita()));
        }
        return toReturn;
    }

    public void clean(ActionEvent event) {
        contaReceber = new ContaReceber();
        contaReceber.setUser(proprietario);
        contaReceber.setStatusReceita(StatusReceita.NAO_RECEBIDA);
        this.salvarParcelas = false;
    }

    //PRIVADOS
    private ContaReceber aumentaParcela(ContaReceber contaReceber) {
        if (contaReceber.getParcelaTotal() > contaReceber.getParcelaAtual()) {
            ContaReceber toReturn = new ContaReceber();
            toReturn.setFormaRecebimento(contaReceber.getFormaRecebimento());
            toReturn.setDataPagamento(aumentaMesDate(contaReceber.getDataPagamento()));
            toReturn.setGrupoReceita(contaReceber.getGrupoReceita());
            toReturn.setObservacao(contaReceber.getObservacao());
            toReturn.setParcelaAtual(contaReceber.getParcelaAtual() + 1);
            toReturn.setParcelaTotal(contaReceber.getParcelaTotal());
            toReturn.setStatusReceita(StatusReceita.NAO_RECEBIDA);
            toReturn.setUser(contaReceber.getUser());
            toReturn.setValor(contaReceber.getValor());
            return toReturn;
        } else {
            return null;
        }
    }

    private Date aumentaMesDate(Date dataVencimento) {
        return UtilMetodos.aumentaMesDate(dataVencimento, 1);
    }

    public ContaReceber getContaReceber() {
        return contaReceber;
    }

    public void setContaReceber(ContaReceber contaReceber) {
        this.contaReceber = contaReceber;
    }

    public void atualizaModelContas(ActionEvent event) {
        this.dataModel = new ListDataModel(this.receitaFinanceiraBean.buscarReceitasNaoRecebidas(proprietario, initBusca, limiteContas));
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

    public boolean isSalvarParcelas() {
        return salvarParcelas;
    }

    public void setSalvarParcelas(boolean salvarParcelas) {
        this.salvarParcelas = salvarParcelas;
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

    public boolean isExibirCadReceita() {
        return exibirCadReceita;
    }

    public void setExibirCadReceita(boolean exibirCadReceita) {
        this.exibirCadReceita = exibirCadReceita;
    }

    
}
