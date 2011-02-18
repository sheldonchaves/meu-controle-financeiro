/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.financeiro.ejbbeans.timeservices;

import br.com.financeiro.ejbbeans.timeservices.interfaces.EmailSendInterface;
import br.com.financeiro.ejbbeans.timeservices.interfaces.TimerContasLocal;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

/**
 *
 * @author gbvbahia
 */
@Stateless
public class TimerContas implements TimerContasLocal {
    @Resource(name = "jms/Financeiro")
    private Queue financeiro;
    @Resource(name = "jms/EmailsFactory")
    private ConnectionFactory emailsFactory;

    @Override
    public void enviarLembreteContaEmail(EmailSendInterface emailSendInterface) {
        try {
            sendJMSMessageToFinanceiro(emailSendInterface);
        } catch (JMSException ex) {
            Logger.getLogger(TimerContas.class.getName()).log(Level.SEVERE, "Erro ao enviar MSG!", ex);
        }
    }
    private Message createJMSMessageForjmsFinanceiro(Session session, Serializable messageData) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage ob = session.createObjectMessage();
        ob.setStringProperty("MessageFormat", "financeiro_msg");
        ob.setObject(messageData);
        return ob;
    }

    private void sendJMSMessageToFinanceiro(Serializable messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = emailsFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(financeiro);
            messageProducer.send(createJMSMessageForjmsFinanceiro(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
