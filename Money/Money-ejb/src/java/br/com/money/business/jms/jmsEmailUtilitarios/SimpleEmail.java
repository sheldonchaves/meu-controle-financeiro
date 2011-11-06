/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.business.jms.jmsEmailUtilitarios;

import br.com.saber.certificacao.ejbs.jms.jmsEmailUtilitarios.interfaces.EmailSendInterface;



/**
 *
 * @author gbvbahia
 */
public class SimpleEmail implements EmailSendInterface{

    private String body;
    private String email;
    private String subject;

    /**
     * Corpo email
     * @return
     */
    @Override
    public String getBody() {
        return this.body;
    }

    /**
     * Email destinatário
     * @return
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Assunto email
     * @return
     */
    @Override
    public String getSubject() {
        return this.subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}