package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.service.mail.Addressee;
import ru.javaops.masterjava.service.mail.MailWSClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@Slf4j
@WebServlet("/mail")
public class MailServlet extends HttpServlet {
    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        returnToMain(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String subject = req.getParameter("subject");
        String body = req.getParameter("email_body");
        Set<Addressee> emailTo= Arrays.stream(req.getParameterValues("emailTo")).map(Addressee::new).collect(Collectors.toSet());
        MailWSClient.sendBulk(emailTo, subject, body);
        returnToMain(req, resp);
    }

    private void returnToMain(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale(),
                ImmutableMap.of("users", userDao.getWithLimit(20)));
        engine.process("mail", webContext, resp.getWriter());
    }
}
