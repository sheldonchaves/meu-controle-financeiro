/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.converter;

import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.modelos.ContaBancaria;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
@FacesConverter(value = "contaBancariaConverter", forClass = ContaBancaria.class)
public class ContaBancariaConverter implements Converter{
    ContaBancariaBeanLocal contaBancariaBean = lookupContaBancariaBeanLocal();

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (StringUtils.isBlank(string)) {
            return string;
        }
        ContaBancaria cb = contaBancariaBean.find(new Long(string));
        if (cb == null) {
            exception(fc);
        }
        return cb;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null) {
            return null;
        } else if (o.toString().equals("")) {
            return "";
        } else if (o instanceof String) {
            return o.toString();
        }
        try {
            ContaBancaria dm = (ContaBancaria) o;
            return dm.getId().toString();
        } catch (ClassCastException e) {
            exception(fc);
        }
        return o.toString();
    }

    private ContaBancariaBeanLocal lookupContaBancariaBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ContaBancariaBeanLocal) c.lookup("java:global/Money/Money-ejb/ContaBancariaBean!br.com.money.business.interfaces.ContaBancariaBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private void exception(FacesContext fc) {
        FacesMessage msg = new FacesMessage(MensagemUtils.getResourceBundle("erroconvertererro", fc));
        msg.setSeverity(FacesMessage.SEVERITY_FATAL);
        throw new ConverterException(msg);
    }

}
