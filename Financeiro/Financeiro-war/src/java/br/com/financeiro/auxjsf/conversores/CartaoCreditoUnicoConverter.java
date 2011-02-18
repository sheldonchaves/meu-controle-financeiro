/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.conversores;

import br.com.financeiro.ejbbeans.interfaces.CartaoCreditoLocal;
import br.com.financeiro.entidades.CartaoCreditoUnico;
import br.com.financeiro.utils.UtilMetodos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author gbvbahia
 */
public class CartaoCreditoUnicoConverter implements Converter{

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
           if (value == null || value.trim().equals("") || value.equals("Selecione")) {
            return value;
        }
        Integer l = null;
        try {
            l = new Integer(value);
        } catch (NumberFormatException e) {
            FacesMessage msg = UtilMetodos.messageFactoring("CartaoCreditoUnicoConverterError", null, FacesContext.getCurrentInstance());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ConverterException(msg);
        }
        CartaoCreditoUnico e = lookupCartaoCreditoBeanLocal().buscarPorID(l);
        if (e == null) {
            FacesMessage msg = UtilMetodos.messageFactoring("CartaoCreditoUnicoConverterError", null, FacesContext.getCurrentInstance());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ConverterException(msg);
        }
        return e;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
          if (value == null) {
            return null;
        }else if(value.equals("")){
            return "";
        }else if(value.equals("Selecione")){
            return "Selecione";
        }
        try {
            CartaoCreditoUnico e = (CartaoCreditoUnico) value;
            return e.getId().toString();
        } catch (ClassCastException e) {
            FacesMessage msg = UtilMetodos.messageFactoring("CartaoCreditoUnicoConverterError", null, FacesContext.getCurrentInstance());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ConverterException(msg);
        }
    }

    private CartaoCreditoLocal lookupCartaoCreditoBeanLocal() {
        try {
            Context c = new InitialContext();
            return (CartaoCreditoLocal) c.lookup("java:comp/env/CartaoCreditoBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }


}
