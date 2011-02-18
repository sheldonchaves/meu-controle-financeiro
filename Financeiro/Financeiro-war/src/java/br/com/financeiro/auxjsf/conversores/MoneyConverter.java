/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.conversores;


import br.com.financeiro.utils.UtilMetodos;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
public class MoneyConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (string == null || string.equals("") || StringUtils.isBlank(string)) {
            return string;
        }
        if (StringUtils.substringBefore(string, ",").contains(",")) {
            exception();
        }else if(!StringUtils.contains(string, ",") && !StringUtils.contains(string, ".")){
            string += ",00";
        }else if(!StringUtils.contains(string, ",") && StringUtils.contains(string, ".")){
            string = string.replace(".", ",");
        }
        try {
            return new Double(string.replace(".", "").replace(",", ".").trim());
        } catch (Exception e) {
            exception();
            return null;
        }
    }

    private void exception() {
        FacesMessage msg = UtilMetodos.messageFactoring("valorMonetarioInvalido", null, FacesContext.getCurrentInstance());
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        throw new ConverterException(msg);
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if(o == null ||StringUtils.isBlank(o.toString()) ) return "0,00";

        Double money = (Double) o;
        return UtilMetodos.currencyFormat(money).replace("R$", "").trim();
    }
}
