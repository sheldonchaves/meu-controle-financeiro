/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf.pessoal;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.beansjsf.provisoes.AcumuladoFaces;
import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.LembreteConta;
import br.com.financeiro.entidades.User;
import br.com.financeiro.excecoes.LembreteContasException;
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
public class LembreteContaFaces {
    @EJB
    private UserLocal userBean;

    private User proprietario;

    private LembreteConta lembreteConta;

    /** Creates a new instance of LembreteContaFaces */
    public LembreteContaFaces() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        Logger.getLogger(AcumuladoFaces.class.getName()).log(Level.INFO, "Um bean LembreteContaFaces acaba de ser criado!");
    }

    public void salvar(ActionEvent event){
        try{
            lembreteConta.setUser(proprietario);
        this.userBean.salvarLembreteContas(lembreteConta);
        UtilMetodos.messageFactoringFull("lembreteContasSalvo", FacesMessage.SEVERITY_INFO, FacesContext.getCurrentInstance());
        }catch(LembreteContasException e){
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
    }

    public LembreteConta getLembreteConta() {
        if(this.lembreteConta == null) this.lembreteConta = userBean.busrcarLembreteContas(proprietario);
        if(this.lembreteConta == null) this.lembreteConta = new LembreteConta();
        return lembreteConta;
    }

    public void setLembreteConta(LembreteConta lembreteConta) {
        this.lembreteConta = lembreteConta;
    }


}
