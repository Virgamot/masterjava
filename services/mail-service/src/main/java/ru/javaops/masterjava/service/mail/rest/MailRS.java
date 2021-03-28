package ru.javaops.masterjava.service.mail.rest;


import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotBlank;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.service.mail.util.Attachments;
import ru.javaops.masterjava.web.WebStateException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.Collections;
import java.util.List;

@Path("/")
@Slf4j
public class MailRS {
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test";
    }

    @POST
    @Path("send")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public GroupResult send(@NotBlank @QueryParam("users") String users,
                            @QueryParam("subject") String subject,
                            @NotBlank @QueryParam("body") String body,
//                            @FormDataParam("attach") InputStream attachInputStream,     some problem with input stream
                            @FormDataParam("attach") File attachedFile,
                            @FormDataParam("attach") FormDataContentDisposition attachMetaData) throws WebStateException {

        List attachments = Collections.emptyList();

        try  {
            InputStream fileIs = new FileInputStream(attachedFile);
            System.out.println(fileIs.available());
            attachments = ImmutableList.of(Attachments.getAttachment(attachMetaData.getFileName(), fileIs));
        } catch (IOException e) {
            log.warn("Wrong attached file.");
            e.printStackTrace();
        }

        return MailServiceExecutor.sendBulk(MailWSClient.split(users), subject, body, attachments);
    }
}