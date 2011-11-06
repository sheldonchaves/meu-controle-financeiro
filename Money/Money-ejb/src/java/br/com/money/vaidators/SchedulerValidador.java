/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.vaidators;

import br.com.money.business.interfaces.SchedulerBeanLocal;
import br.com.money.exceptions.SchedulerException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.Scheduler;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import javax.ejb.Stateless;
import org.apache.commons.validator.EmailValidator;
/**
 *
 * @author Guilherme
 */
@Stateless(name = "schedulerValidador")
public class SchedulerValidador implements ValidadorInterface<Scheduler, SchedulerBeanLocal> {

    @Override
    public void validar(Scheduler entidade, SchedulerBeanLocal bean, Object object) throws ValidacaoException {
        if (entidade == null) {
            lancarException("SchedulerInvalido", "Scheduler");
        }
        if (entidade.getEmail() == null) {
            lancarException("shcedulerEmailNulo", "E-mail");
        }
        if (entidade.getEmail().length() > Scheduler.QUANTIDADE_CARACTERES_EMAIL) {
            lancarException("shcedulerEmailLongo", "E-mail");
        }
        if(!isValidEmail(entidade.getEmail())){
            lancarException("shcedulerEmailInvalido", "E-mail");
        }
        if (entidade.getUser() == null) {
            lancarException("shcedulerUserNulo", "E-mail");
        }
    }

    private void lancarException(String msg, String atributo) {
        SchedulerException ex = new SchedulerException(msg, atributo);
        throw ex;
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
