/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.business.jms.jmsEmailUtilitarios.interfaces;

import br.com.saber.certificacao.ejbs.jms.jmsEmailUtilitarios.interfaces.EmailSendInterface;
import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface EmailSendLocal {

    void enviarEmailJMS(EmailSendInterface emailSendInterface);
    
}
