/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.converter;

import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.money.business.interfaces.DetalheUsuarioBeanLocal;
import br.com.money.modelos.DetalheMovimentacao;
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
@FacesConverter(value = "detalheMovimentacaoConverter")
public class DetalheMovimentacaoConverter implements Converter {

    DetalheUsuarioBeanLocal detalheUsuarioBean = lookupDetalheUsuarioBeanLocal();

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (StringUtils.isBlank(string)) {
            return string;
        }
        DetalheMovimentacao dm = detalheUsuarioBean.find(new Long(string));
        if (dm == null) {
            exception(fc);
        }
        return dm;
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
            DetalheMovimentacao dm = (DetalheMovimentacao) o;
            return dm.getId().toString();
        } catch (ClassCastException e) {
            exception(fc);
        }
        return o.toString();
    }

    private void exception(FacesContext fc) {
        FacesMessage msg = new FacesMessage(MensagemUtils.getResourceBundle("erroconvertererro", fc));
        msg.setSeverity(FacesMessage.SEVERITY_FATAL);
        throw new ConverterException(msg);
    }

    private DetalheUsuarioBeanLocal lookupDetalheUsuarioBeanLocal() {
        try {
            Context c = new InitialContext();
            return (DetalheUsuarioBeanLocal) c.lookup("java:global/Money/Money-ejb/DetalheUsuarioBean!br.com.money.business.interfaces.DetalheUsuarioBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
