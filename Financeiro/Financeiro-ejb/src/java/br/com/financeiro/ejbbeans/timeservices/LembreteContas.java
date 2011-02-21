/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.ejbbeans.timeservices;

import br.com.financeiro.ejbbeans.timeservices.interfaces.EmailSendInterface;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author gbvbahia
 */
@MessageDriven(mappedName = "jms/Financeiro", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "MessageFormat = 'financeiro_msg'")
})
public class LembreteContas implements MessageListener {

    public LembreteContas() {
    }

    @Override
    public void onMessage(Message message) {
        EmailSendInterface email = null;
        ObjectMessage objMsg = (ObjectMessage) message;
        try {
            email = (EmailSendInterface) objMsg.getObject();
            sendMail(email.getEmail(), email.getSubject(), email.getBody());
        } catch (JMSException ex) {
            Logger.getLogger(LembreteContas.class.getName()).log(Level.SEVERE, "onMessage 1º catch", ex);
        } catch (MessagingException msgEx) {
            Logger.getLogger(LembreteContas.class.getName()).log(Level.SEVERE, "onMessage 2º catch", msgEx);
        } catch (NamingException nEx) {
            Logger.getLogger(LembreteContas.class.getName()).log(Level.SEVERE, "onMessage 3º catch", nEx);
        }
    }

    private void sendMail(String email, String subject, String body) throws NamingException, MessagingException {
        SimpleAuth auth = new SimpleAuth("gbvbahia01@gmail.com", "xxx");
        Properties p = new Properties();
        p.put("mail.host", "gmail-smtp.l.google.com");
        p.put("mail.debug", "false");
        p.put("mail.smtp.auth", "true");
        p.put("mail.mime.charset", "ISO-8859-1");
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "gmail-smtp.l.google.com");
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.socketFactory.fallback", "false");
        Session session = session = Session.getInstance(p, auth);
        session.setDebug(false);
        //Logger.getLogger(LembreteContas.class.getName()).log(Level.WARNING, "******* ATENÇÃO ATENÇÃO ******   RETIRAR DEBUG DO E-MAIL!");
        MimeMessage message = new MimeMessage(session);
        String characterCoding = "iso-8859-1";
        String assunto = null;
        try {
            assunto = MimeUtility.encodeText(subject, characterCoding, null);
        } catch (Exception e) {
            Logger.getLogger(LembreteContas.class.getName()).log(Level.SEVERE, "Impossivel codigicar titulo do e-mail!", e);
        }
        if (assunto != null) {
            message.setSubject(assunto);
        } else {
            message.setSubject(subject);
        }
        message.setRecipient(RecipientType.TO, new InternetAddress(email));
        message.setSentDate(new Date());
        message.setText(body, characterCoding);
        message.setHeader("Content-Type", "text/html; charset=" + characterCoding);
        Transport.send(message);
    }

    private class SimpleAuth extends Authenticator {

        public String username = null;
        public String password = null;

        public SimpleAuth(String user, String pwd) {
            username = user;
            password = pwd;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
