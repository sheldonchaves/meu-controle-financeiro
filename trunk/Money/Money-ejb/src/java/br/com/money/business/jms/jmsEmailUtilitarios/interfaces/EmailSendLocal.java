/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.business.jms.jmsEmailUtilitarios.interfaces;

import javax.ejb.Local;

/**
 *
 * @author gbvbahia
 */
@Local
public interface EmailSendLocal {

    void enviarEmailJMS(EmailSendInterface emailSendInterface);
    
}
