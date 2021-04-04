package ru.javaops.masterjava.webapp.akka;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailRemoteService;
import ru.javaops.masterjava.service.mail.util.MailUtils;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static ru.javaops.masterjava.webapp.WebUtil.createMailObject;
import static ru.javaops.masterjava.webapp.WebUtil.doAndWriteResponse;

@Slf4j
public class TypedSendWorker implements Runnable {
    private final AsyncContext context;
    private MailRemoteService mailService;

    public TypedSendWorker(AsyncContext context, MailRemoteService mailService) {
        this.context = context;
        this.mailService = mailService;
    }

    @Override
    public void run() {
        log.info(">TypedSendWorker: started");
        HttpServletResponse resp = (HttpServletResponse) this.context.getResponse();
        HttpServletRequest req = (HttpServletRequest) this.context.getRequest();
        resp.setContentType("text/plain");

        try {
            doAndWriteResponse(resp, () -> sendAkka(createMailObject(req)));
        } catch (IOException e) {
            log.warn("Error akka sending: "+e.getLocalizedMessage());
        }

        this.context.complete();
        log.info(">TypedSendWorker: ended");
    }

    private String sendAkka(MailUtils.MailObject mailObject) throws Exception {
        scala.concurrent.Future<GroupResult> future = mailService.sendBulk(mailObject);
        log.info("Receive future, waiting result ...");
        GroupResult groupResult = Await.result(future, Duration.create(10, "seconds"));
        return groupResult.toString();
    }
}
