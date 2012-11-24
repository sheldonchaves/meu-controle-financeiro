/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.common;

import br.com.gbvbahia.financeiro.beans.business.interfaces.AccessControlBusiness;
import br.com.gbvbahia.financeiro.modelos.Grupo;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.projeto.web.jsfutil.JsfUtil;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Gerencia o usuário logado.
 *
 * @since v.1 21/05/2012
 * @author Guilherme
 */
@ManagedBean(name = "loginController")
@RequestScoped
public class LoginController implements Serializable {

    @EJB
    private AccessControlBusiness accessControl;

    /**
     * True se for do grupo Admin.
     * @return 
     */
    public boolean isAdmin() {
        return accessControl.getUsuarioLogado().getGrupos().contains(new Grupo("admins"));
    }

    /**
     * True se for do grupo de usuário.
     * @return 
     */
    public boolean isUser() {
        return accessControl.getUsuarioLogado().getGrupos().contains(new Grupo("users"));
    }

    /**
     *
     * @return Usuario.
     */
    public Usuario getUsuario() {
        return accessControl.getUsuarioLogado();
    }

    /**
     * Sai do sistema, matando a sessão e retornando para página de login.
     *
     * @return JsfUtil.LOGIN_PAGE
     */
    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().
                getExternalContext().getSession(false);
        session.invalidate();
        return JsfUtil.SAIR_PAGE;
    }
}
