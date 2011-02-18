/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.timeservices.utilitarias;

import br.com.financeiro.ejbbeans.timeservices.interfaces.EmailSendInterface;

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
    public String getBody() {
        return this.body;
    }

    /**
     * Email destinatário
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Assunto email
     * @return
     */
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
