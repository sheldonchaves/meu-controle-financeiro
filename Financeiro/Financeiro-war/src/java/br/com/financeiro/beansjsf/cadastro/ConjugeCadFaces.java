/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.cadastro;

import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.ProprietarioConjugeException;
import br.com.financeiro.utils.UtilMetodos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class ConjugeCadFaces {

    @EJB
    private UserLocal userBean;

    private String loginConjuge;

    private String senhaConjuge;

    private User proprietario;

    /** Creates a new instance of ConjugeCadFaces */
    public ConjugeCadFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
    }

    public void addConjuge(ActionEvent event){
        User userConjuge = userBean.buscaProprietarioLogin(loginConjuge, false);
        if(userConjuge == null || !userConjuge.getPassword().equals(userBean.criptografarSenha(senhaConjuge, userConjuge.stringAMIN()))){
            UtilMetodos.messageFactoringFull("conjugeNaoLocalizado", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(ConjugeCadFaces.class.getName()).log(Level.WARNING, "Tentativa de cadastro de conjuge do: " + this.proprietario.getLogin() + " sem sucesso!\nConjuge informado: " + this.loginConjuge + "Motivo: Senha incorreta.");
            return;
        }
        try {
            userBean.adicionarConjuge(proprietario, userConjuge);
            ControleObserver.notificaObservers(proprietario);
            UtilMetodos.messageFactoringFull("conjugeaddok", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
            clean();
        } catch (ProprietarioConjugeException ex) {
            FacesMessage msg = new FacesMessage(ex.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(ConjugeCadFaces.class.getName()).log(Level.WARNING, "Tentativa de cadastro de conjuge do: " + this.proprietario.getLogin() + " sem sucesso!\nConjuge informado: " + this.loginConjuge + "Motivo: Senha OK, Exceção userBean.");
            clean();
        }
    }

    public void removerConjuge(ActionEvent event){
        try {
            this.userBean.removerConjuge(proprietario);
            UtilMetodos.messageFactoringFull("conjugeremoveok", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        } catch (ProprietarioConjugeException ex) {
            FacesMessage msg = new FacesMessage(ex.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            clean();
        }
    }

    private void clean(){
        this.loginConjuge = null;
        this.senhaConjuge = null;
    }

    public User getProprietario() {
        return proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }

    public String getLoginConjuge() {
        return loginConjuge;
    }

    public void setLoginConjuge(String loginConjuge) {
        this.loginConjuge = loginConjuge;
    }

    public String getSenhaConjuge() {
        return senhaConjuge;
    }

    public void setSenhaConjuge(String senhaConjuge) {
        this.senhaConjuge = senhaConjuge;
    }
}
