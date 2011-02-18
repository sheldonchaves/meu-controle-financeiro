/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.listeners;

import br.com.financeiro.ejbbeans.timeservices.interfaces.TimeServiceLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author gbvbahia
 */
public class IniciaTimerVencimentos implements ServletContextListener{

    public void contextDestroyed(ServletContextEvent sce) {
        
    }

    public void contextInitialized(ServletContextEvent sce) {
        TimeServiceLocal tl = lookupTimeServiceBeanLocal();
        tl.iniciarVerificacaoContas();
        Logger.getLogger(IniciaTimerVencimentos.class.getName()).log(Level.INFO, "Iniciado TimerService Referente a próximos vencimentos!");
    }

    private TimeServiceLocal lookupTimeServiceBeanLocal() {
        try {
            Context c = new InitialContext();
            return (TimeServiceLocal) c.lookup("java:comp/env/TimeServiceBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }


}
