package ru.javaops.masterjava.service.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;
import ru.javaops.masterjava.service.MailTestData;
import ru.javaops.masterjava.service.model.EmailResult;

import java.util.List;

import static org.junit.Assert.*;
import static ru.javaops.masterjava.service.MailTestData.EMAIL_RESULTS;

public class MailDaoTest extends AbstractDaoTest<MailDao> {

    public MailDaoTest() {
        super(MailDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        MailTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        MailTestData.setUp();
    }

    @Test
    public void insertGeneratedId() {

    }

    @Test
    public void getAll() {
        final List<EmailResult> emailResults = dao.getAll();
        assertEquals(EMAIL_RESULTS, emailResults);
        System.out.println(emailResults);
    }

    @Test
    public void insert() {
    }
}
