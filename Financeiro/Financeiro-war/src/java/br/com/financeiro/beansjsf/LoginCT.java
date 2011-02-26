/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.beansjsf;

import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.enums.TipoConta;
import br.com.financeiro.excecoes.ContaBancariaAgenciaException;
import br.com.financeiro.excecoes.ContaBancariaExistenteException;
import br.com.financeiro.excecoes.ContaBancariaNomeBancoException;
import br.com.financeiro.excecoes.ContaBancariaNumeroContaException;
import br.com.financeiro.excecoes.ContaBancariaObservacaoException;
import br.com.financeiro.excecoes.ContaBancariaProprietarioException;
import br.com.financeiro.excecoes.ContaBancariaSaldoException;
import br.com.financeiro.excecoes.ContaBancariaTipoContaException;
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
 * @author Guilherme
 */
public class LoginCT {

    @EJB
    private UserLocal proprietarioBean;

    public static final String SESSION_PROPRIETARIO = "user_logado";
    public static final String SESSION_ADMIN = "ADMIN";
    private User proprietario;

    public String logar() {
        Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, Criptografia.encodePassword("102030", "ADMIN"));
        User prop = proprietarioBean.buscaProprietarioLogin(proprietario.getLogin(), false);
        if (prop != null && prop.getPassword().equals(this.proprietarioBean.criptografarSenha(proprietario.getPassword(), prop.stringAMIN()))) {
            insereProprietarioSession(prop);
            return "principal";
        } else {
            UtilMetodos.messageFactoringFull("loginInvalido", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(LoginCT.class.getName()).log(Level.WARNING, "Tentativa de login sem sucesso!");
            return null;
        }
    }

    public String deslogar(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.invalidate();
        return "deslogar";
    }

    private void insereProprietarioSession(User proprietario){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(SESSION_PROPRIETARIO, proprietario);
        if(proprietario.stringAMIN().equals("ADMIN")){
            session.setAttribute(SESSION_ADMIN, SESSION_ADMIN);
        }
    }

    /** Creates a new instance of LoginCT */
    public LoginCT() {
        proprietario = new User();
    }

    public User getProprietario() {
        return proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }
    //Somente para criação de banco
    @EJB
    private ContaBancariaLocal contaBancariaBean;
    
    public void criaBanco(ActionEvent event) {
        User pro = new User();
        pro.setLogin("hjohh");
        pro.setFirst_name("Guilherme");
        pro.setLast_name("Braga");
        //Você receberá um email com sua senha neste e-mail.
        //Configure br.com.financeiro.ejbbeans.timeservices.LembreteContas
        //Para enviar e-mail, a senha não está correta.
        pro.setEmail("gbvbahia01@hotmail.com");
        try {
            proprietarioBean.salvarproprietario(pro, getURLFinanceiro());
        } catch (ProprietarioSenhaException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProprietarioNomeException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProprietarioLoginException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ProprietarioException pe){
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, pe);
        }
        pro = proprietarioBean.buscaProprietarioLogin("gbvbahia", true);
        ContaBancaria cb = new ContaBancaria();
        cb.setAgencia("40017");
        cb.setNumeroConta("701238");
        cb.setNomeBanco("Banco do Brasil");
        cb.setObservacao("Conta corrente no Banco do Brasil");
        cb.setSaldo(0.00);
        cb.setStatus(true);
        cb.setTipoConta(TipoConta.CONTA_CORRENTE);
        cb.setUser(pro);
        pro.getContasBancarias().add(cb);
        try {
            contaBancariaBean.salvaContaBancaria(cb);
        } catch (ContaBancariaProprietarioException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaNomeBancoException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaTipoContaException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaAgenciaException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaNumeroContaException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaSaldoException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaObservacaoException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ContaBancariaExistenteException ex) {
            Logger.getLogger(LoginCT.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesMessage msg = new FacesMessage("Tabelas criadas e dados inseridos!");
        FacesMessage msg2 = new FacesMessage("Você receberá um email informando sua senha!");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg2.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().addMessage(null, msg2);
    }
    private String getURLFinanceiro(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String toReturn = UtilMetodos.getResourceBundle("paginaFinanceiro", FacesContext.getCurrentInstance());
        toReturn += session.getServletContext().getContextPath();
        return toReturn;
    }
}
