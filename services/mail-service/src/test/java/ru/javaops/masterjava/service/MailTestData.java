package ru.javaops.masterjava.service;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.dao.MailDao;
import ru.javaops.masterjava.service.model.EmailResult;

import java.util.Arrays;
import java.util.List;

public class MailTestData {
    public static EmailResult YANDEX_EMAIL;
    public static EmailResult MAILRU_EMAIL;
    public static EmailResult GOOGLE_EMAIL;

    public static List<EmailResult> EMAIL_RESULTS;

    public static void init() {
        YANDEX_EMAIL = new EmailResult("admin@yandex.ru", "test", "test", "success");
        MAILRU_EMAIL = new EmailResult("admin@mail.ru", "test", "test", "success");
        GOOGLE_EMAIL = new EmailResult("admin@google.ru", "test", "test", "success");
        EMAIL_RESULTS = Arrays.asList(GOOGLE_EMAIL, MAILRU_EMAIL, YANDEX_EMAIL);
    }

    public static void setUp() {
        MailDao dao = DBIProvider.getDao(MailDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            EMAIL_RESULTS.forEach(dao::insert);
        });
    }
}
