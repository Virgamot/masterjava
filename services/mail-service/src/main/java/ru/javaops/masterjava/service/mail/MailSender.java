package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.dao.MailDao;
import ru.javaops.masterjava.service.model.EmailResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class MailSender {

    private static final MailDao mailDao = DBIProvider.getDao(MailDao.class);
    private static final Properties MAIL_PROPERTIES;

    static {
        MAIL_PROPERTIES = new Properties();
        URL propertiesLocation = MailSender.class.getClassLoader().getResource("mail.properties");
        assert propertiesLocation != null;
        try (InputStream input = new FileInputStream(propertiesLocation.getFile())) {
            MAIL_PROPERTIES.load(input);
        } catch (IOException ex) {
            log.error("Error during mail.properties reading: " + ex.getMessage());
        }
    }

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {

        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        List<String> recipientEmails = new ArrayList<>();
        recipientEmails.addAll(to.stream().map(Addressee::getEmail).collect(Collectors.toList()));
        recipientEmails.addAll(cc.stream().map(Addressee::getEmail).collect(Collectors.toList()));
        String[] recipientEmailsArr = recipientEmails.toArray(new String[0]);

        Email email = new SimpleEmail();
        email.setHostName(MAIL_PROPERTIES.getProperty("mail.host"));
        email.setSmtpPort(Integer.parseInt(MAIL_PROPERTIES.getProperty("mail.port")));
        email.setAuthentication(MAIL_PROPERTIES.getProperty("mail.username"), MAIL_PROPERTIES.getProperty("mail.password"));
        email.setSSLOnConnect(Boolean.parseBoolean(MAIL_PROPERTIES.getProperty("mail.useSSL")));
        email.setDebug(Boolean.parseBoolean(MAIL_PROPERTIES.getProperty("mail.debug")));

        boolean emailSended = true;

        try {
            email.setFrom(MAIL_PROPERTIES.getProperty("mail.username"));
            email.setSubject(subject);
            email.setMsg(body);
            email.addTo(recipientEmailsArr);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
            log.error("Error during sending email: " + e.getMessage());
            emailSended = false;
        }

        EmailResult emailResult = new EmailResult();
        emailResult.setEmailTo(String.join(",", recipientEmailsArr));
        emailResult.setSubject(subject);
        emailResult.setBody(body);
        emailResult.setResult(emailSended ? "SUCCESS" : "FAILED");
        mailDao.insert(emailResult);
    }
}
