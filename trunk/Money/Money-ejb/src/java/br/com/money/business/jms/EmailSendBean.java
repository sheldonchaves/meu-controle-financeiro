/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.money.business.jms;


import br.com.money.business.jms.jmsEmailUtilitarios.interfaces.EmailSendLocal;
import br.com.saber.certificacao.ejbs.jms.jmsEmailUtilitarios.interfaces.EmailSendInterface;
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
 *  O bean de MSG está no projeto saber e irei reaproveita-lo 
 * por enquanto...
 * @author gbvbahia
 */
@Stateless
public class EmailSendBean implements EmailSendLocal {
    @Resource(mappedName="jms/EmailSaber")
    private Queue financeiro;
    @Resource(name = "jms/EmailsFactory")
    private ConnectionFactory emailsFactory;

    @Override
    public void enviarEmailJMS(EmailSendInterface emailSendInterface) {
        try {
            sendJMSMessageToFinanceiro(emailSendInterface);
        } catch (JMSException ex) {
            Logger.getLogger(EmailSendBean.class.getName()).log(Level.SEVERE, "Erro ao enviar Email para: " + emailSendInterface.getEmail(), ex);
        }
    }
    private Message createJMSMessageForjmsFinanceiro(Session session, Serializable messageData) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage ob = session.createObjectMessage();
        ob.setStringProperty("MessageFormat", "saber_msg");
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
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
