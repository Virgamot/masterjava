package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

import static ru.javaops.masterjava.persist.UserTestData.FIST5_USERS;
import static ru.javaops.masterjava.persist.UserTestData.compareUsers;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        Assert.assertEquals(FIST5_USERS, users);
    }

    @Test
    public void insertAllTest() throws Exception {
        dao.clean();
        dao.insertAll(FIST5_USERS, 2);
        List<User> users = dao.getWithLimit(5);
        compareUsers(users, FIST5_USERS);
    }
}