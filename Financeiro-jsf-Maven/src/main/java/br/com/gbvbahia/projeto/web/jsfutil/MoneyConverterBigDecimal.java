/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.jsfutil;


import br.com.gbvbahia.financeiro.utils.NumberUtils;
import br.com.gbvbahia.utils.MensagemUtils;
import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guilherme
 */
@FacesConverter(value = "moneyBigConverter")
public class MoneyConverterBigDecimal implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (string == null || string.equals("") || StringUtils.isBlank(string)) {
            return string;
        }
        if (StringUtils.substringBefore(string, ",").contains(",")) {
            exception(fc);
        }else if(!StringUtils.contains(string, ",") && !StringUtils.contains(string, ".")){
            string += ",00";
        }else if(!StringUtils.contains(string, ",") && StringUtils.contains(string, ".")){
            string = string.replace(".", ",");
        }
        try {
            return new BigDecimal(string.replace(".", "").replace(",", ".").trim());
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
        if(o == null ||StringUtils.isBlank(o.toString()) ) return "0,00";

        BigDecimal money = (BigDecimal) o;
        return NumberUtils.currencyFormat(money.doubleValue()).replace("R$", "").trim();
    }
}
