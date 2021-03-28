package ru.javaops.masterjava.service.mail.listeners;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;
import ru.javaops.masterjava.service.mail.jms.JmsEmail;
import ru.javaops.masterjava.service.mail.util.Attachments;
import ru.javaops.masterjava.web.WebStateException;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.ByteArrayInputStream;

@WebListener
@Slf4j
public class JmsMailListener implements ServletContextListener {
    private Thread listenerThread = null;
    private QueueConnection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            InitialContext initCtx = new InitialContext();
            QueueConnectionFactory connectionFactory =
                    (QueueConnectionFactory) initCtx.lookup("java:comp/env/jms/ConnectionFactory");
            connection = connectionFactory.createQueueConnection();
            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) initCtx.lookup("java:comp/env/jms/queue/MailQueue");
            ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) connectionFactory;
            activeMQConnectionFactory.setTrustAllPackages(true);
            QueueReceiver receiver = queueSession.createReceiver(queue);
            connection.start();
            log.info("Listen JMS messages ...");
            listenerThread = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        Message m = receiver.receive();
                        ObjectMessage om = (ObjectMessage) m;
                        JmsEmail jmsEmail = (JmsEmail) om.getObject();
                        sendEmail(jmsEmail);
                    }
                } catch (Exception e) {
                    log.error("Receiving messages failed: " + e.getMessage(), e);
                }
            });
            listenerThread.start();
        } catch (Exception e) {
            log.error("JMS failed: " + e.getMessage(), e);
        }
    }

    private void sendEmail(JmsEmail jmsEmail) throws WebStateException {
        Byte[] jmsBytes = jmsEmail.getBytes();
        byte[] bytes = new byte[jmsBytes.length];
        for (int i = 0; i < jmsBytes.length; i++) {
            bytes[i] = jmsBytes[i];
        }
        MailServiceExecutor.sendBulk(jmsEmail.getEmailsTo(), jmsEmail.getSubject(), jmsEmail.getBody(),
                ImmutableList.of(Attachments.getAttachment(jmsEmail.getAttachedFileName(), new ByteArrayInputStream(bytes))));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                log.warn("Couldn't close JMSConnection: ", ex);
            }
        }
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }
}