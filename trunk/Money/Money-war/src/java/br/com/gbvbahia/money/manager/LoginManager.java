/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.UsuarioBeanLocal;
import br.com.money.modelos.Usuario;
import br.com.money.utils.Criptografia;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
@ManagedBean(name = "loginManager")
@SessionScoped
public class LoginManager implements InterfaceManager {

    @EJB
    private UsuarioBeanLocal usuarioBean;

    public static final String SESSION_PROPRIETARIO = "user_logado";

    public static final String SESSION_ADMIN = "ADMIN";

    private Usuario usuario;

    /** Creates a new instance of LoginManager */
    public LoginManager() {
    }

    public String logar() {
        Usuario prop = usuarioBean.buscarUsuarioByLogin(usuario.getLogin());
        if (prop != null && prop.getPassword().equals(this.usuarioBean.criptografarSenha(usuario.getPassword(), prop.stringAMIN()))) {
            insereProprietarioSession(prop);
            this.usuario = prop;
            return "principal";
        } else {
            UtilMetodos.messageFactoringFull("loginInvalido", FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(LoginManager.class.getName()).log(Level.WARNING, "Tentativa de login, {0}, sem sucesso!", usuario.getLogin());
            return null;
        }
    }

    public String deslogar() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.invalidate();
        return "deslogar";
    }

    private void insereProprietarioSession(Usuario proprietario) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(SESSION_PROPRIETARIO, proprietario);
        if (proprietario.stringAMIN().equals("ADMIN")) {
            session.setAttribute(SESSION_ADMIN, SESSION_ADMIN);
        }
    }

    //====================
    // Iniciadores
    //====================
    @Override
    @PreDestroy
    public void end() {
    }

    @PostConstruct
    @Override
    public void init() {
        this.usuario = new Usuario();
//        //EM DESENVOLVIMENTO
//         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "***   REMOVER LOGIN E SENHA DE TESTE   ***");
//         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "***   REMOVER LOGIN E SENHA DE TESTE   ***");
//         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "***   REMOVER LOGIN E SENHA DE TESTE   ***");
//         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "***   REMOVER LOGIN E SENHA DE TESTE   ***");
//         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "***   REMOVER LOGIN E SENHA DE TESTE   ***");
//         Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "***   REMOVER LOGIN E SENHA DE TESTE   ***");
//        usuario.setLogin("gbvbahia");
//        usuario.setPassword("102030");
    }

    //=========================
    //Getters AND Setters
    //=========================

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
