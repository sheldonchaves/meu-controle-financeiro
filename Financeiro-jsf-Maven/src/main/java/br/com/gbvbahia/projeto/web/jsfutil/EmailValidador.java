/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.projeto.web.jsfutil;

import br.com.gbvbahia.utils.MensagemUtils;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.validator.EmailValidator;

/**
 *
 * @author Guilherme
 */
@FacesValidator(value = "emailValidador")
public class EmailValidador implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object o) throws ValidatorException {
        if (o == null) {
            return;
        }
        String email = o.toString();
        if (!EmailValidator.getInstance().isValid(email)) {
            FacesMessage msg = new FacesMessage(MensagemUtils.getResourceBundle("emailValidadorErro", context));
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
