/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.listeners;

import br.com.financeiro.auxjsf.observadores.ControleObserver;
import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.entidades.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author gbvbahia
 */
public class SessionListenr implements HttpSessionListener{

    public void sessionCreated(HttpSessionEvent se) {
    Logger.getLogger(SessionListenr.class.getName()).log(Level.INFO, "Uma sessão acaba de ser criada!");
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        User proprietario = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        ControleObserver.removeSessionClose(proprietario);
        Logger.getLogger(SessionListenr.class.getName()).log(Level.INFO, "Uma sessão acaba de ser destruída!");
    }

}
