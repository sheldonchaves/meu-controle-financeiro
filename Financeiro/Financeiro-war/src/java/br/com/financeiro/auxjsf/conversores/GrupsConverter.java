/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.conversores;

import br.com.financeiro.ejbbeans.interfaces.UserLocal;
import br.com.financeiro.entidades.Grups;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author gbvbahia
 */
public class GrupsConverter implements Converter {
    private UserLocal userBean;;

    public GrupsConverter() {
         userBean = this.lookupUserBeanLocal();
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        return userBean.getBrupBayRole(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        Grups g = (Grups) value;
        return g.getGroupName();
    }

    private UserLocal lookupUserBeanLocal() {
        try {
            Context c = new InitialContext();
            return (UserLocal) c.lookup("java:comp/env/UserBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
