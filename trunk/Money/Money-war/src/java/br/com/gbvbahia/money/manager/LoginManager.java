/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.manager;

import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.UsuarioBeanLocal;
import br.com.money.modelos.Usuario;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
@ManagedBean(name = "loginManager")
@SessionScoped
public class LoginManager {

    @EJB
    private UsuarioBeanLocal usuarioBean;

    public static final String SESSION_PROPRIETARIO = "user_logado";

    public static final String SESSION_ADMIN = "ADMIN";

    private Usuario usuario;
    
    private static Map<String, Integer> mapProtector = new HashMap<String, Integer>();
    /** Creates a new instance of LoginManager */
    public LoginManager() {
    }

    public String logar() {
        Usuario prop = usuarioBean.buscarUsuarioByLogin(usuario.getLogin());
        if (prop != null && prop.getPassword().equals(this.usuarioBean.criptografarSenha(usuario.getPassword(), prop.stringAMIN()))) {
            insereProprietarioSession(prop);
            removerMapProtector();
            this.usuario = prop;
            return "principal";
        } else {
            MensagemUtils.messageFactoringFull("loginInvalido", null, FacesMessage.SEVERITY_ERROR, FacesContext.getCurrentInstance());
            Logger.getLogger(LoginManager.class.getName()).log(Level.WARNING, "Tentativa de login, {0}, sem sucesso!", usuario.getLogin());
            verificaTentativaInvalidaDeLogin();
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
    @PreDestroy
    public void end() {
    }

    @PostConstruct
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

    
        /**
     * Caso alguém coloque um robô para tentar logar terá que esperar cada vez mais pelo retorno
     * da tentativa de login
     */
    private void verificaTentativaInvalidaDeLogin() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = req.getRemoteAddr();
        int time = 1;
        if (mapProtector.containsKey(ip)) {
            time = mapProtector.get(ip) * 4;
            mapProtector.put(ip, mapProtector.get(ip) + 1);
        } else {
            mapProtector.put(ip, time);
        }
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, "*** Thread Login: ***", ex);
        }
    }

    private void removerMapProtector() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = req.getRemoteAddr();
        mapProtector.remove(ip);
    }
    
    public Locale getLocale() {
        return SelectItemManager.BRASIL;
    }

    public String getPattern() {
        return SelectItemManager.PATTERN;
    }   
}
