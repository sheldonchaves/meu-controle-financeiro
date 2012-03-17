/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.converter;

import br.com.gbvbahia.money.utils.MensagemUtils;
import br.com.gbvbahia.money.utils.UtilMetodos;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
@FacesConverter(value = "moneyConverter")
public class MoneyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (string == null || string.equals("") || StringUtils.isBlank(string)) {
            return string;
        }
        boolean negativo = StringUtils.contains(string, "-");
        if (negativo) {
            string = StringUtils.remove(string, "-");
            string = StringUtils.trim(string);
        }
        if (StringUtils.substringBefore(string, ",").contains(",")) {
            exception(fc);
        } else if (!StringUtils.contains(string, ",") && !StringUtils.contains(string, ".")) {
            string += ",00";
        } else if (!StringUtils.contains(string, ",") && StringUtils.contains(string, ".")) {
            string = string.replace(".", ",");
        }
        try {
            Double toReturn =
                    new Double(string.replace(".", "").replace(",", ".").trim());
            if (negativo) {
                return toReturn * -1;
            } else {
                return toReturn;
            }
        } catch (Exception e) {
            exception(fc);
            return null;
        }
    }

    private void exception(FacesContext fc) {
        FacesMessage msg = new FacesMessage(MensagemUtils.getResourceBundle("valorMonetarioInvalido", fc));
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        throw new ConverterException(msg);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null || StringUtils.isBlank(o.toString())) {
            return "0,00";
        }

        Double money = (Double) o;
        return UtilMetodos.currencyFormat(money).replace("R$", "").trim();
    }
}
