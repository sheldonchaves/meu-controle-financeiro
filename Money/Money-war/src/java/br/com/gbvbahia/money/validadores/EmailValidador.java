/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.validadores;

import br.com.gbvbahia.money.utils.UtilMetodos;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.validator.EmailValidator;

/**
 *  Validador JSF para realizar validação de emails
 * @author gbvbahia
 */
@FacesValidator(value="emailValidador")
public class EmailValidador implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
          if(o == null)return;
        String email = o.toString();
        if(!EmailValidator.getInstance().isValid(email)){
            FacesMessage msg = new FacesMessage(UtilMetodos.getResourceBundle("shcedulerEmailInvalido", fc));
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}