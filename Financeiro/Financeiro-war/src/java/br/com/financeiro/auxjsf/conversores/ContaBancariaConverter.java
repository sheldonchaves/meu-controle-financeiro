/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.conversores;

import br.com.financeiro.ejbbeans.interfaces.ContaBancariaLocal;
import br.com.financeiro.entidades.ContaBancaria;
import br.com.financeiro.entidades.enums.TipoConta;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gbvbahia
 */
public class ContaBancariaConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals("")) {
            return value;
        }
        ContaBancaria cb = lookupContaBancariaBeanLocal().buscaContabancaria(getAgenciaConta(value)[0], getAgenciaConta(value)[1]);
        return cb;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        if (value.equals("")) {
            return "";
        }
        ContaBancaria cb = (ContaBancaria) value;
        return cb.getLabel();
    }

    private ContaBancariaLocal lookupContaBancariaBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ContaBancariaLocal) c.lookup("java:comp/env/ContaBancariaBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private String[] getAgenciaConta(String string) {
        String[] toReturn = new String[2];
        String ag = StringUtils.substringBefore(string, "/");
        for(TipoConta fp : TipoConta.values()){
            if(ag.contains(fp.toString())){
                ag = StringUtils.substringAfter(ag, fp.toString());
            }
        }
        String cc = StringUtils.substringAfter(string, "/");
        toReturn[0] = StringUtils.trim(ag);
        toReturn[1] = StringUtils.trim(cc);
        return toReturn;
    }
}
