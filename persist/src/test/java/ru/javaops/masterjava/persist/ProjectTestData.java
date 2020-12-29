package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class ProjectTestData {
    
    public static Project TOPJAVA=new Project("Topjava");
    public static Project MASTERJAVA=new Project("Masterjava");
    public static List<Project> PROJECTS= ImmutableList.of(MASTERJAVA,TOPJAVA);


    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            PROJECTS.forEach(dao::insert);
        });
    }
}
