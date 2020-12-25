package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

public class GroupTestData {
    public static Group TOPJAVA = new Group("Topjava", GroupType.FINISHED);
    public static Group MASTERJAVA = new Group("Masterjava", GroupType.CURRENT);
    public static List<Group> GROUPS = ImmutableList.of(MASTERJAVA, TOPJAVA);

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
        });
    }
}
