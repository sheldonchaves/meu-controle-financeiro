/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.jms;

import br.com.gbvbahia.financeiro.beans.facades.EmailPropertiesFacade;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendBusiness;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendInterface;
import br.com.gbvbahia.financeiro.modelos.EmailProperties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author Guilherme
 */
@Stateless
public class EmailSendBean implements EmailSendBusiness {

    @EJB
    private EmailPropertiesFacade emailPropertiesBean;
    private EmailProperties emailProperties;

    @PostConstruct
    public void carregarEmailProperties() {
        for (EmailProperties e : emailPropertiesBean.findAll()) {
            if (e.isContaAtiva()) {
                emailProperties = e;
                return;
            }
        }
    }

    @Override
    @Asynchronous
    public void enviarEmailJMSAsynchronous(EmailSendInterface emailSendInterface) {
        try {
            sendEmailEmailInterface(emailSendInterface);
        } catch (EmailException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "Erro ao enviar E-MAIL para: " + emailSendInterface.getEmail(), ex);
        }
    }

    @Override
    public void enviarEmailJMS(EmailSendInterface emailSendInterface) {
        try {
            sendEmailEmailInterface(emailSendInterface);
        } catch (EmailException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "Erro ao enviar E-MAIL para: " + emailSendInterface.getEmail(), ex);
        }
    }

    private void sendEmail(EmailSendInterface emailSendInterface) {
        try {
            sendEmailEmailInterface(emailSendInterface);
        } catch (EmailException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
                    "Erro ao enviar E-MAIL para: " + emailSendInterface.getEmail(), ex);
        }
    }

    private void sendEmailEmailInterface(EmailSendInterface messageData) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setHostName(emailProperties.getHostName());
        email.setCharset(emailProperties.getCharacterCoding());
        email.setSmtpPort(emailProperties.getSmtpPort());
        email.setAuthenticator(new DefaultAuthenticator(emailProperties.getLoginEmail(), emailProperties.getSenhaEmail()));
        email.setTLS(emailProperties.isTls());
        email.setHtmlMsg(messageData.getBody() + "</br> http://sabercertificacao.com.br/money");
        email.addTo(messageData.getEmail());
        email.setFrom(emailProperties.getFromEmail(), emailProperties.getAssuntoDefault());
        email.setSubject(messageData.getSubject());
        email.setSocketConnectionTimeout(1000 * 60 * 3);
        email.setSocketTimeout(1000 * 60 * 3);
        email.setSSL(emailProperties.isSsl());
        email.setSslSmtpPort(emailProperties.getSslSmtpPort());
        email.send();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Email enviado com sucesso! ==> '{'{0}'}'", messageData.getEmail());
    }
}
