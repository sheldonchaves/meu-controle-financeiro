/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.beans.jms;

import br.com.gbvbahia.financeiro.beans.aop.LogTime;
import br.com.gbvbahia.financeiro.beans.facades.EmailPropertiesFacade;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendBusiness;
import br.com.gbvbahia.financeiro.beans.jms.interfaces.EmailSendInterface;
import br.com.gbvbahia.financeiro.modelos.EmailProperties;
import br.com.gbvbahia.financeiro.utils.Encryption;
import br.com.gbvbahia.financeiro.utils.FileUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RunAs;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @RunAs("sys") Deve ser criado um usuario com grupo sys no filerealm do
 * servidor para que
 * @RunAs funcione.
 * @author Guilherme
 */
@Stateless
@Interceptors({LogTime.class})
@RunAs("sys")
public class EmailSendBean implements EmailSendBusiness {

    @EJB
    private EmailPropertiesFacade emailPropertiesBean;

    private EmailProperties carregarEmailProperties() {
        return emailPropertiesBean.buscarEmailAtivo();
    }

    @Override
    @Asynchronous
    public void enviarEmailJMSAsynchronous(EmailSendInterface emailSendInterface) {
        sendEmail(emailSendInterface);
    }

    @Override
    public void enviarEmailJMS(EmailSendInterface emailSendInterface) {
        sendEmail(emailSendInterface);
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
        EmailProperties emailProperties = carregarEmailProperties();
        if (emailProperties == null) {
            throw new EmailException("EmailProperties NAO PODE SER NULL!!!!");
        }
        HtmlEmail email = new HtmlEmail();

        email.setDebug(false);
        email.setHostName(emailProperties.getHostName());
        email.setCharset(emailProperties.getCharacterCoding());
        email.setSmtpPort(emailProperties.getSmtpPort());
        email.setAuthenticator(new DefaultAuthenticator(emailProperties.getLoginEmail(), Encryption.decrypting(emailProperties.getSenhaEmail())));
        email.setTLS(emailProperties.isTls());
        if (FileUtils.LOGO_EMAIL_FILE != null && FileUtils.LOGO_EMAIL_FILE.exists()) {
            String cid = email.embed(FileUtils.LOGO_EMAIL_FILE, "Money Logo");
            if (messageData.addUrlBody()) {
                email.setHtmlMsg("<img src=\"cid:" + cid + "\"><br></br>" + messageData.getBody() + "</br> http://sabercertificacao.com.br/money");
            } else {
                email.setHtmlMsg("<img src=\"cid:" + cid + "\"><br></br>" + messageData.getBody());
            }
        } else {
            if (messageData.addUrlBody()) {
                email.setHtmlMsg(messageData.getBody() + "</br> http://sabercertificacao.com.br/money");
            } else {
                email.setHtmlMsg(messageData.getBody());
            }
        }
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
