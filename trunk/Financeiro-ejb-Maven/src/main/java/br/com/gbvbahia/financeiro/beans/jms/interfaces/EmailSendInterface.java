/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.jms.interfaces;

import java.io.Serializable;

/**
 *
 * @author Guilherme
 */
public interface EmailSendInterface extends Serializable {

    /**
     * Email do destinatário.
     *
     * @return
     */
    public String getEmail();

    /**
     * Assunto do email.
     *
     * @return
     */
    public String getSubject();

    /**
     * Corpo do e-mail
     *
     * @return
     */
    public String getBody();
    /**
     * Define se deve ser inserido uma url no finald o corpo da MSG
     * @return 
     */
    public boolean addUrlBody();
}
