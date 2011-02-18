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
 * @author gbvbahia
 */
public class NumeroCartaoCreditoConversor implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
           if (StringUtils.isBlank(value)) {
            return value;
        }
           String numeroCartao = value.replace(".", "").replace("-", "").replace("/", "");
           if(!StringUtils.isNumeric(numeroCartao)){
               throw new ConverterException(UtilMetodos.messageFactoring("CartaoCreditoNumeroInvalidoException", FacesMessage.SEVERITY_ERROR, context));
           }
           return numeroCartao;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
          if (value == null) {
            return null;
        }
          String numeroCartao = value.toString();
          String toReturn = "";
          int numeros = numeroCartao.length();
          if(numeros % 4 == 0){
              numeros = 4;
          }else{
              numeros = 3;
          }
          for(int i = 1; i <= numeroCartao.length(); i++){
            toReturn += numeroCartao.charAt(i -1);
            if(i > 1 &&  i % numeros == 0 && i < (numeroCartao.length() - 1)){
                toReturn += ".";
            }
          }
          return toReturn;
    }


}
