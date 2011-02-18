/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.pessoal;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;
import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.ProprietarioException;
import br.com.financeiro.excecoes.ProprietarioLoginException;
import br.com.financeiro.excecoes.ProprietarioNomeException;
import br.com.financeiro.excecoes.ProprietarioSenhaException;
import br.com.financeiro.utils.Criptografia;
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
public class SenhaFaces {

    @EJB
    private UserLocal userBean;
    private String senhaAtual;
    private String novaSenha;
    private String confirmaSenha;
    private User proprietario;

    /** Creates a new instance of SenhaFaces */
    public SenhaFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        //Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.INFO, "Um bean SenhaFaces acaba de ser criado!");
    }

    public void salvar(ActionEvent event) {
        if (proprietario.getPassword().equals(Criptografia.encodePassword(senhaAtual, proprietario.stringAMIN()))) {
            if (novaSenha.equals(confirmaSenha)) {
                proprietario.setPassword(novaSenha);
                try {
                    userBean.alteraSenhaUser(proprietario, getURLFinanceiro(), false);
                    UtilMetodos.messageFactoringFull("senhaAlterada", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
                } catch (ProprietarioSenhaException ex) {
                    FacesMessage msg = new FacesMessage(ex.getMessage());
                    msg.setSeverity(FacesMessage.SEVERITY_WARN);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return;
                } catch (ProprietarioNomeException ex) {
                    FacesMessage msg = new FacesMessage(ex.getMessage());
                    msg.setSeverity(FacesMessage.SEVERITY_WARN);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return;
                } catch (ProprietarioLoginException ex) {
                    FacesMessage msg = new FacesMessage(ex.getMessage());
                    msg.setSeverity(FacesMessage.SEVERITY_WARN);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return;
                }catch (ProprietarioException pe){
                    FacesMessage msg = new FacesMessage(pe.getMessage());
                    msg.setSeverity(FacesMessage.SEVERITY_WARN);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return;
                }
            } else {
                UtilMetodos.messageFactoringFull("senhanovaConfirmaErro", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            }
        } else {
            UtilMetodos.messageFactoringFull("senhaAtualIncorreta", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
        }

    }

    private String getURLFinanceiro(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return UtilMetodos.getURLFinanceiro(session, FacesContext.getCurrentInstance());
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }
}
