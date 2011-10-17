/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.observador.ControleObserver;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.DetalheMovimentacao;
import java.util.List;
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
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
@ManagedBean(name = "detalheMovimentacao")
@ViewScoped
public class DetalheMovimentacaoManager implements InterfaceManager {

    @EJB
    private DetalheUsuarioBeanLocal detalheUsuarioBean;

    @ManagedProperty("#{loginManager}")
    private LoginManager loginManager;

    //@ManagedProperty("#{selectItemManager}")
    //private SelectItemManager selectItemManager;

    private HtmlInputText input;

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
        detalheMovimentacao = new DetalheMovimentacao();
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "DetalheMovimentacaoManager.init() executado!");
    }

    @PreDestroy
    @Override
    public void end() {
        Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "DetalheMovimentacaoManager.end() executado!");
    }

    //====================
    //Métodos de Negócio
    //====================
    public void clean() {
        this.detalheMovimentacao = new DetalheMovimentacao();
        this.input.setSubmittedValue("");
    }

    public void salvarDetalheMovimentacao() {
        try {
            detalheUsuarioBean.salvarDetalheMovimentacao(detalheMovimentacao);
            clean();
            UtilMetodos.messageFactoringFull("detalheMovimentacaoCadastradoOk", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            ControleObserver.notificaObservers(null, ControleObserver.Eventos.CAD_DETALHE_MOVIMENTACAO);
        } catch (ValidacaoException v) {
            if (!StringUtils.isBlank(v.getAtributoName())) {
                UtilMetodos.messageFactoringFull(UtilMetodos.getResourceBundle(v.getMessage(), FacesContext.getCurrentInstance()), null, v.getAtributoName(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            } else {
                UtilMetodos.messageFactoringFull(v.getMessage(), FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            }
        }
    }

    //====================
    //Table Actions
    //====================
    //====================
    //SelectItem
    //====================
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
}
