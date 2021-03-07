package ru.javaops.masterjava.service.mail;

import ru.javaops.web.WebStateException;

import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import java.util.Set;

@MTOM
@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService", targetNamespace = "http://mail.javaops.ru/"
//          , wsdlLocation = "WEB-INF/wsdl/mailService.wsdl"
)
public class MailServiceImpl implements MailService {
    public String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body, byte[] attachment) throws WebStateException {
        return MailSender.sendToGroup(to, cc, subject, body, attachment);
    }

    @Override
    public GroupResult sendBulk(Set<Addressee> to, String subject, String body, byte[] attachment) throws WebStateException {
        return MailServiceExecutor.sendBulk(to, subject, body, attachment);
    }
}