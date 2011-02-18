/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.conversores;

import br.com.financeiro.entidades.enums.FormaRecebimento;
import br.com.financeiro.utils.UtilMetodos;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 *
 * @author Guilherme
 */
public class EnumFormaRecebimentoConverter implements Converter{
       public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        FormaRecebimento[] enumS = FormaRecebimento.values();
        for (FormaRecebimento e : enumS) {
            if (e.toString().equals(value)) {
                return e;
            }
        }
        FacesMessage msg = UtilMetodos.messageFactoring("enumConverterErro", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
        throw new ConverterException(msg);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }else if(value.equals("")){
            return "";
        }else if (value instanceof String) {
            String enumDis = (String) value;
            FormaRecebimento[] enumS = FormaRecebimento.values();
            for (FormaRecebimento e : enumS) {
                if (e.toString().equals(enumDis)) {
                    return e.toString();
                }
            }
        }
        try {
            FormaRecebimento e = (FormaRecebimento) value;
            return e.toString();
        } catch (ClassCastException e) {
            FacesMessage msg = UtilMetodos.messageFactoring("enumConverterErro", FacesMessage.SEVERITY_WARN, FacesContext.getCurrentInstance());
            throw new ConverterException(msg);
        }
    }
}
