/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.conversores;

import br.com.financeiro.ejbbeans.interfaces.GrupoFinanceiroLocal;
import br.com.financeiro.entidades.detalhes.GrupoGasto;
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
public class GrupoGastoConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        return lookupGrupoFinanceiroBeanLocal().buscarGrupoGastoPorNome(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        if (value.equals("")) {
            return "";
        }
        GrupoGasto gg = (GrupoGasto) value;
        return gg.getGrupoGasto();
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
