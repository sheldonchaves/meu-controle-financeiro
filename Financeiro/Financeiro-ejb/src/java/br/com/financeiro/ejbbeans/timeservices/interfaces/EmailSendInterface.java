/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.timeservices.interfaces;

import java.io.Serializable;

/**
 *
 * @author gbvbahia
 */
public interface EmailSendInterface extends Serializable{


    /**
     * Email do destinatário.
     * @return
     */
    public String getEmail();
    /**
     * Assunto do email.
     * @return
     */
    public String getSubject();
    /**
     * Corpo do e-mail
     * @return
     */
    public String getBody();
}
