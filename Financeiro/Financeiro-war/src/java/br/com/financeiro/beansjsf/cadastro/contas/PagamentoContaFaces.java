/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.cadastro.contas;

import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.ejbbeans.interfaces.ContaPagarReceberLocal;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.ContaPagar;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
import br.com.financeiro.entidades.enums.FormaPagamento;
import br.com.financeiro.entidades.enums.StatusPagamento;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class PagamentoContaFaces  implements Observer{
    @EJB
    private CartaoCreditoLocal cartaoCreditoBean;
    @EJB
    private ContaBancariaLocal contaBancariaBean;
    @EJB
    private GrupoFinanceiroLocal grupoFinanceiroBean;
    @EJB
    private ContaPagarReceberLocal contaPagarReceberBean;
    
    private ContaPagar contaPagar;
    private Locale locale = new Locale("pt", "BR");
    private User proprietario;
    private boolean salvarParcelas;
    private DataModel dataModel;
    private boolean renderedCartaoCredito = false;
    private boolean renderedContaTransferencia = false;
    private boolean exibirCadConta = false;
    private boolean exibirCadContaDiaria = false;
    private int diaria = 7;
    private boolean excluirParcelamento = false;
    private Date initBusca;
    private int limiteContas = 10;
    
    public PagamentoContaFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.addBeanObserver(proprietario, this);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        this.initBusca = c.getTime();
        clean(null);
    }

    public void pegarConta(ActionEvent event){
        this.contaPagar = (ContaPagar) this.dataModel.getRowData();
    }

    public void sugerirDataCC(ActionEvent event){
        this.contaPagar.setDataVencimento(this.contaPagar.getCartaoCreditoUnico().getProximoVencimento());
    }

    public void apagar(ActionEvent event) {
        try {
            if(excluirParcelamento){
            this.contaPagarReceberBean.deletarContaPagarReceberPorIdentificador(this.contaPagar.getIdentificador(), this.contaPagar.getContaParcelaAtual());
            }else{
            this.contaPagarReceberBean.deletarContaPagarReceber(this.contaPagar);
            }
            clean(event);
            ControleObserver.notificaObservers(proprietario);
            UtilMetodos.messageFactoringFull("contaPagarApagaga", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } catch (ContaPagarReceberValueException e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "ContaPagarReceberValueException");
        }
    }

    public void salvar(ActionEvent event) {
        try {
            String identificador = UtilMetodos.getIdentificadorUnico(this.proprietario.getId(), this.contaPagar.getDataVencimento());
            this.contaPagar.setIdentificador(identificador);
            this.contaPagarReceberBean.salvarContaPagarReceber(contaPagar);
            if (salvarParcelas) {
                ContaPagar debito = null;
                for (debito = contaPagar; (debito = aumentaParcela(debito)) != null;) {
                    this.contaPagarReceberBean.salvarContaPagarReceber(debito);
                }
            }
            UtilMetodos.messageFactoringFull("contaPagarSalva", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            clean(event);
            ControleObserver.notificaObservers(proprietario);
        } catch (ContaPagarReceberValueException e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(PagamentoContaFaces.class.getName()).log(Level.INFO, "ContaPagarReceberValueException");
        }
    }

    public void clean(ActionEvent event) {
        contaPagar = new ContaPagar();
        contaPagar.setUser(proprietario);
        contaPagar.setStatusPagamento(StatusPagamento.NAO_PAGA);
        this.diaria = 7;
        this.salvarParcelas = false;
        this.renderedCartaoCredito = false;
        this.renderedContaTransferencia = false;
        this.exibirCadConta = false;
        this.exibirCadContaDiaria = false;
        this.excluirParcelamento = false;
    }

    public List<SelectItem> getGrupoGastoItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (GrupoGasto gg : this.grupoFinanceiroBean.buscarGrupoGastoParaSelecao(proprietario)) {
            toReturn.add(new SelectItem(gg, gg.getGrupoGasto()));
        }
        return toReturn;
    }

    public List<SelectItem> getFormaPagamentoItens(){
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for(FormaPagamento fp : FormaPagamento.values()){
            toReturn.add(new SelectItem(fp, fp.toString()));
        }
        return toReturn;
    }

    public List<SelectItem> getContaBancarias(){
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for(ContaBancaria cb : this.contaBancariaBean.contasProprietario(proprietario)){
            toReturn.add(new SelectItem(cb, cb.getLabel()));
        }
        return toReturn;
    }

    public List<SelectItem> getCartaoCreditoItens(){
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        toReturn.add(new SelectItem("Selecione", "Selecione"));
        for(CartaoCreditoUnico cc : this.cartaoCreditoBean.buscaCartaoPorStatus(true,proprietario)){
            toReturn.add(new SelectItem(cc, cc.getLabelCartao()));
        }
        return toReturn;
    }

    public void formaPagamentoListener(ValueChangeEvent event){
        if(event.getNewValue().equals(FormaPagamento.CARTAO_DE_CREDITO)){
            this.renderedCartaoCredito = true;
            this.renderedContaTransferencia = false;
        }else if(event.getNewValue().equals(FormaPagamento.TRASFERENCIA_ELETRONICA)){
            this.renderedCartaoCredito = false;
            this.renderedContaTransferencia = true;
        }else{
            this.renderedCartaoCredito = false;
            this.renderedContaTransferencia = false;
        }
    }

    //Getters and Setters
    public ContaPagar getContaPagar() {
        return contaPagar;
    }

    public void setContaPagar(ContaPagar contaPagar) {
        this.contaPagar = contaPagar;
    }
   
    public Locale getLocale() {
        return locale;
    }

    public boolean isSalvarParcelas() {
        return salvarParcelas;
    }

    public void setSalvarParcelas(boolean salvarParcelas) {
        this.salvarParcelas = salvarParcelas;
    }

       public void atualizaModelContas(ActionEvent event){
        this.dataModel = new ListDataModel(this.contaPagarReceberBean.buscarContasNaoPagas(proprietario, initBusca, limiteContas));
    }

    public DataModel getDataModel() {
        if(this.dataModel == null) atualizaModelContas(null);
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public boolean isRenderedCartaoCredito() {
        return renderedCartaoCredito;
    }

    public void setRenderedCartaoCredito(boolean renderedCartaoCredito) {
        this.renderedCartaoCredito = renderedCartaoCredito;
    }

    public boolean isRenderedContaTransferencia() {
        return renderedContaTransferencia;
    }

    public void setRenderedContaTransferencia(boolean renderedContaTransferencia) {
        this.renderedContaTransferencia = renderedContaTransferencia;
    }

    //PRIVADOS
    private ContaPagar aumentaParcela(ContaPagar contaPagar) {
        if (contaPagar.getParcelaTotal() > contaPagar.getParcelaAtual()) {
            ContaPagar toReturn = new ContaPagar();
            toReturn.setDataVencimento(aumentaDate(contaPagar.getDataVencimento()));
            toReturn.setGrupoGasto(contaPagar.getGrupoGasto());
            toReturn.setJuros(contaPagar.getJuros());
            toReturn.setObservacao(contaPagar.getObservacao());
            toReturn.setParcelaAtual(contaPagar.getParcelaAtual() + 1);
            toReturn.setParcelaTotal(contaPagar.getParcelaTotal());
            toReturn.setStatusPagamento(StatusPagamento.NAO_PAGA);
            toReturn.setUser(contaPagar.getUser());
            toReturn.setValor(contaPagar.getValor());
            toReturn.setCartaoCreditoUnico(contaPagar.getCartaoCreditoUnico());
            toReturn.setContaPara(contaPagar.getContaPara());
            toReturn.setFormaPagamento(contaPagar.getFormaPagamento());
            toReturn.setIdentificador(contaPagar.getIdentificador());
            return toReturn;
        } else {
            return null;
        }
    }

    private Date aumentaDate(Date dataVencimento) {
        if(this.exibirCadContaDiaria == true){
            return UtilMetodos.aumentaDiaDate(dataVencimento, diaria);
        }
        return UtilMetodos.aumentaMesDate(dataVencimento, 1);
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

    public boolean isExibirCadConta() {
        return exibirCadConta;
    }

    public void setExibirCadConta(boolean exibirCadConta) {
        this.exibirCadConta = exibirCadConta;
    }

    public boolean isExcluirParcelamento() {
        return excluirParcelamento;
    }

    public void setExcluirParcelamento(boolean excluirParcelamento) {
        this.excluirParcelamento = excluirParcelamento;
    }

    public boolean isExibirCadContaDiaria() {
        return exibirCadContaDiaria;
    }

    public void setExibirCadContaDiaria(boolean exibirCadContaDiaria) {
        if(exibirCadContaDiaria){
            setSalvarParcelas(exibirCadContaDiaria);
        }
        this.exibirCadContaDiaria = exibirCadContaDiaria;
    }

    public int getDiaria() {
        return diaria;
    }

    public void setDiaria(int diaria) {
        this.diaria = diaria;
    }
}
