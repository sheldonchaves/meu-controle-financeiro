/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.converter;

import br.com.gbvbahia.money.utils.UtilMetodos;
import br.com.money.enums.TipoConta;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Guilherme
 */
@FacesConverter(value = "tipoContaConverter")
public class TipoContaConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
          if (value == null || value.trim().equals("")) {
            return value;
        }
        TipoConta[] enumS = TipoConta.values();
        for (TipoConta e : enumS) {
            if (e.toString().equals(value)) {
                return e;
            }
        }
        FacesMessage msg = new FacesMessage(UtilMetodos.getResourceBundle("enumConverterErro", fc));
        msg.setSeverity(FacesMessage.SEVERITY_FATAL);
        throw new ConverterException(msg);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if (value == null) {
            return null;
        } else if (value.equals("")) {
            return "";
        } else if (value instanceof String) {
            String enumDis = (String) value;
            TipoConta[] enumS = TipoConta.values();
            for (TipoConta e : enumS) {
                if (e.toString().equals(enumDis)) {
                    return e.toString();
                }
            }
        }
        try {
            TipoConta e = (TipoConta) value;
            return e.toString();
        } catch (ClassCastException e) {
            FacesMessage msg = new FacesMessage(UtilMetodos.getResourceBundle("enumConverterErro", fc));
            msg.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ConverterException(msg);
        }
    }
    
}
