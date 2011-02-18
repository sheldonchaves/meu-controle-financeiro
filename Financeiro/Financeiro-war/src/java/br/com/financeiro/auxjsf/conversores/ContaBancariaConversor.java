/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.auxjsf.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Guilherme
 */
public class ContaBancariaConversor implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("")) {
            return value;
        }
        String conta = value.replace(".", "").replace("-", "").replace("_", "");
        return conta;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }

        String conta = value.toString();
        if (conta.equals("")) {
            return conta;
        }
        if (!conta.contains("/")) {
            int posicao = conta.length() - 2;
            if (posicao > 0) {
                return conta.substring(0, posicao + 1) + "-" + conta.substring(posicao + 1);
            }
        } else {
            int posicao = StringUtils.indexOf(conta, "/");
            if (posicao > 0) {
                return conta.substring(0, posicao -1) + "-" + conta.substring(posicao -1);
            }
        }


        return conta;
    }
}
