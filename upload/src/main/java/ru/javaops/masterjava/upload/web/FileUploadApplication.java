package ru.javaops.masterjava.upload.web;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.upload.xml.schema.User;
import ru.javaops.masterjava.upload.xml.util.JaxbParser;
import ru.javaops.masterjava.upload.xml.util.StaxStreamProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class FileUploadApplication {
    public void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(),
                request.getLocale());
        FileUploadServlet.getTemplateEngine().process("upload", ctx, response.getWriter());
    }

    public void processUploadedFile(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StaxStreamProcessor processor = new StaxStreamProcessor(req.getPart("file").getInputStream());
            JaxbParser parser = new JaxbParser(User.class);
            Set<User> users = new HashSet<>();

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                User user = parser.unmarshal(processor.getReader(), User.class);
                users.add(user);
            }

            users.forEach(u -> System.out.println(String.format("{Name: %s email: %s flag: %s}",
                    u.getValue(),
                    u.getEmail(),
                    u.getFlag())));

        } catch (Exception ex) {
            System.out.println("Can't parse users from loaded file:\n" + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

}
