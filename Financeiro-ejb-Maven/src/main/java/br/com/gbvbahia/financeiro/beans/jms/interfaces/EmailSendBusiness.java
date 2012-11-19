/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.jms.interfaces;

import javax.ejb.Local;

/**
 *
 * @author Guilherme
 */
@Local
public interface EmailSendBusiness {

    /**
     * Envia email de forma assincrona.
     *
     * @param emailSendInterface Qualquer objeto que implemente essa
     * interface.
     */
    void enviarEmailJMSAsynchronous(EmailSendInterface emailSendInterface);

    /**
     * Envia email normal, aguardando a resposta de envio do método.
     *
     * @param emailSendInterface
     */
    void enviarEmailJMS(EmailSendInterface emailSendInterface);
}
