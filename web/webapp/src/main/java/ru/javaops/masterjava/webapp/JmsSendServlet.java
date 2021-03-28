package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.IOUtils;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.service.mail.jms.JmsEmail;
import ru.javaops.masterjava.service.mail.util.Attachments;
import ru.javaops.masterjava.web.WsClient;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.lang.IllegalStateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebServlet("/sendJms")
@Slf4j
@MultipartConfig
public class JmsSendServlet extends HttpServlet {
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            InitialContext initCtx = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initCtx.lookup("java:comp/env/jms/ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer((Destination) initCtx.lookup("java:comp/env/jms/queue/MailQueue"));
        } catch (Exception e) {
            throw new IllegalStateException("JMS init failed", e);
        }
    }

    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                log.warn("Couldn't close JMSConnection: ", ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        try {
            log.info("Start sending");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            String users = req.getParameter("users");
            String subject = req.getParameter("subject");
            String body = req.getParameter("body");
            Part filePart = req.getPart("attach");

//            List attachments = filePart == null ? Collections.emptyList() :
//                    ImmutableList.of(Attachments.getAttachment(filePart.getSubmittedFileName(), filePart.getInputStream()));

            result = sendJms(users, subject, body, filePart);
            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }

    private synchronized String sendJms(String users, String subject, String body, Part attachment) throws JMSException, IOException {

//        TextMessage testMessage = session.createTextMessage();
//        testMessage.setText(subject);
        ObjectMessage objectMessage = session.createObjectMessage();
        String fileName = attachment.getSubmittedFileName();
        byte[] byteArray = IOUtils.toByteArray(attachment.getInputStream());
        Byte[] bytes = new Byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            bytes[i]=byteArray[i];
        }
//        File tempFile = File.createTempFile("jms_attach", "tmp");
//        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
//            IOUtils.copy(attachment.getInputStream(), outputStream);
//        }

        JmsEmail email = new JmsEmail(subject, body, fileName, bytes, MailWSClient.split(users));
//        JmsEmail email = new JmsEmail(subject, body, fileName, tempFile, MailWSClient.split(users));
        objectMessage.setObject(email);
        producer.send(objectMessage);
//        tempFile.deleteOnExit();
        return "Successfully sent JMS message";
    }
}