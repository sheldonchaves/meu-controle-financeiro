/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business.jms;

import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendInterface;
import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *  O bean de MSG está no projeto saber e irei reaproveita-lo 
 * por enquanto...
 * @author gbvbahia
 */
@Stateless
public class EmailSendBean implements EmailSendLocal {

    private String characterCoding = "iso-8859-1";
    @Resource(name = "emailSenha")
    private String senhaEmail;

    @Override
    @Asynchronous
    public void enviarEmailJMS(EmailSendInterface emailSendInterface) {
        sendEmail(emailSendInterface);
    }

    private void sendEmail(EmailSendInterface emailSendInterface) {
        try {
            sendEmailEmailInterface(emailSendInterface);
        } catch (EmailException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Erro ao enviar E-MAIL para: " + emailSendInterface.getEmail(), ex);
        }
    }

    private void sendEmailEmailInterface(EmailSendInterface messageData) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setHostName("gmail-smtp.l.google.com");
        email.setCharset(characterCoding);
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("gbvbahia01@gmail.com", senhaEmail));
        email.setTLS(true);
        email.setHtmlMsg(messageData.getBody());
        email.addTo(messageData.getEmail());
        email.setFrom("gbvbahia01@gmail.com", "Controle Financeiro");
        email.setSubject(messageData.getSubject());
        email.setSocketConnectionTimeout(1000 * 60 * 3);
        email.setSocketTimeout(1000 * 60 * 3);
        email.setSSL(true);
        email.setSslSmtpPort("465");
        email.send();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Email enviado com sucesso! ==> '{'{0}'}'", messageData.getEmail());
    }
}
