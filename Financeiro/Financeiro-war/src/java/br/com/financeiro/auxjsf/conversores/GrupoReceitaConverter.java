/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.conversores;

import br.com.financeiro.beansjsf.LoginCT;
import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.entidades.User;
import br.com.financeiro.entidades.detalhes.GrupoReceita;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gbvbahia
 */
public class GrupoReceitaConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        User user = (User) session.getAttribute(LoginCT.SESSION_PROPRIETARIO);
        return lookupGrupoFinanceiroBeanLocal().buscarGrupoReceitaPorNome(value, user);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        if (value.equals("")) {
            return "";
        }
        if(value instanceof GrupoReceita){
        GrupoReceita gg = (GrupoReceita) value;
        return gg.getGrupoReceita();
        }else{
            return value.toString();
        }
    }

    private GrupoFinanceiroLocal lookupGrupoFinanceiroBeanLocal() {
        try {
            Context c = new InitialContext();
            return (GrupoFinanceiroLocal) c.lookup("java:comp/env/GrupoFinanceiroBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
