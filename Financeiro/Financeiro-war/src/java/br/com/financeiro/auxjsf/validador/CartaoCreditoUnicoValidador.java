/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.auxjsf.validador;

import br.com.financeiro.utils.UtilMetodos;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author gbvbahia
 */
public class CartaoCreditoUnicoValidador implements Validator{

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value == null) return;
        if(value.toString().equals("Selecione")){
            FacesMessage msg = UtilMetodos.messageFactoring("CartaoCreditoSelecionadoException", FacesMessage.SEVERITY_ERROR, context);
            throw new ValidatorException(msg);
        }
    }
}


