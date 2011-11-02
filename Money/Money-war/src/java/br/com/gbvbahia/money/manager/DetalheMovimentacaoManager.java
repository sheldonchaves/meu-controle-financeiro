/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.enums.TipoMovimentacao;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@ManagedBean(name = "detalheMovimentacao")
@ViewScoped
public class DetalheMovimentacaoManager implements InterfaceManager, Observer {

    public static final int CARACTERES_DETALHE_MOVIMENTACAO_LIMIT = 25;
    
    @EJB
    private DetalheUsuarioBeanLocal detalheUsuarioBean;
    
    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;
    
    @ManagedProperty("#{selectItemManager}")
    private SelectItemManager selectItemManager;
    
    private HtmlInputText input;
    private HtmlSelectOneMenu selctTipoPagamento;
    
    private DetalheMovimentacao detalheMovimentacao;
    private List<DetalheMovimentacao> detalhes;

    /** Creates a new instance of DetalheMovimentacaoManager */
    public DetalheMovimentacaoManager() {
    }

    //====================
    // Iniciadores
    //====================
    @PostConstruct
    @Override
    public void init() {
        clean();
        ControleObserver.addBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "DetalheMovimentacaoManager.init() executado!");
    }

    @PreDestroy
    @Override
    public void end() {
        ControleObserver.removeBeanObserver(loginManager.getUsuario(), this);
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "DetalheMovimentacaoManager.end() executado!");
    }

    //====================
    //Métodos de Negócio
    //====================
    @Override
    public void update(Observable o, Object arg) {
        int[] args = (int[]) arg;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO) {
                atualizarModel();
            }
        }
    }

    public void clean() {
        this.detalheMovimentacao = new DetalheMovimentacao();
        this.detalheMovimentacao.setUsuarioProprietario(loginManager.getUsuario());
        if (input != null) {
            this.input.setSubmittedValue("");
        }
        if(selctTipoPagamento != null){
            selctTipoPagamento.setSubmittedValue(UtilMetodos.getResourceBundle("selecione", FacesContext.getCurrentInstance()));
            selctTipoPagamento.setValue(null);
        }
    }

    public void salvarDetalheMovimentacao() {
        try {
            detalheUsuarioBean.salvarDetalheMovimentacao(detalheMovimentacao);
            UtilMetodos.messageFactoringFull("detalheMovimentacaoCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(loginManager.getUsuario(), ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO);
            clean();
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
        }
    }

    private void atualizarModel() {
        this.detalhes = this.detalheUsuarioBean.buscarDetalheMovimentacaoPorUsuario(this.loginManager.getUsuario());
        Collections.sort(detalhes);
    }

    //====================
    //Table Actions
    //====================
    public void bloqDesbloqDetalhe(DetalheMovimentacao det) {
        det.setAtivo(!det.isAtivo());
        this.detalheMovimentacao = det;
        salvarDetalheMovimentacao();
    }
    
    public void alteraDetalheMovimentacaoTipo(DetalheMovimentacao det){
        if(det.getTipoMovimentacao().equals( TipoMovimentacao.DEPOSITO)){
            det.setTipoMovimentacao(TipoMovimentacao.RETIRADA);
        }else{
            det.setTipoMovimentacao(TipoMovimentacao.DEPOSITO);
        }
        this.detalheMovimentacao = det;
        salvarDetalheMovimentacao();
    }

    //====================
    //SelectItem
    //====================
    public List<SelectItem> getTipoPagamento(){
        return selectItemManager.getTipoMovimentacao();
    }
    //=========================
    //Getters AND Setters
    //=========================
    public DetalheMovimentacao getDetalheMovimentacao() {
        return detalheMovimentacao;
    }

    public void setDetalheMovimentacao(DetalheMovimentacao detalheMovimentacao) {
        this.detalheMovimentacao = detalheMovimentacao;
    }

    public HtmlInputText getInput() {
        return input;
    }

    public void setInput(HtmlInputText input) {
        this.input = input;
    }

    public List<DetalheMovimentacao> getDetalhes() {
        if (this.detalhes == null) {
            atualizarModel();
        }
        return detalhes;
    }

    public void setDetalhes(List<DetalheMovimentacao> detalhes) {
        this.detalhes = detalhes;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public int getCaracteresLimit() {
        return CARACTERES_DETALHE_MOVIMENTACAO_LIMIT;
    }

    public SelectItemManager getSelectItemManager() {
        return selectItemManager;
    }

    public void setSelectItemManager(SelectItemManager selectItemManager) {
        this.selectItemManager = selectItemManager;
    }

    public HtmlSelectOneMenu getSelctTipoPagamento() {
        return selctTipoPagamento;
    }

    public void setSelctTipoPagamento(HtmlSelectOneMenu selctTipoPagamento) {
        this.selctTipoPagamento = selctTipoPagamento;
    }
}
