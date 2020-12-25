package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

import static ru.javaops.masterjava.persist.GroupTestData.GROUPS;

public class GroupDaoTest extends AbstractDaoTest<GroupDao> {

    public GroupDaoTest(){ super(GroupDao.class); }

    @Before
    public void setUp() throws Exception
    {
        GroupTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<Group> groups = dao.getWithLimit(2);
        Assert.assertEquals(GROUPS, groups);
    }
}
