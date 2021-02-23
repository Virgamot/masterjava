package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ProjectGroupProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);

    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException, JAXBException {
        val newGroups = new ArrayList<Group>();
        val unmarshaller = jaxbParser.createUnmarshaller();

        while (processor.startElement("Project", "Projects")) {
            ru.javaops.masterjava.xml.schema.Project xmlProject = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.Project.class);
            Project project = new Project();
            project.setName(xmlProject.getName());
            project.setDescription(xmlProject.getDescription());
            projectDao.insert(project);

            int projectId = project.getId();

            xmlProject.getGroup().forEach(
                    g -> {
                        Group group = new Group();
                        group.setProjectId(projectId);
                        group.setName(g.getName());
                        group.setType(GroupType.valueOf(g.getType().name()));
                        newGroups.add(group);
                    }
            );
        }
        log.info("Insert batch " + newGroups);
        groupDao.insertBatch(newGroups);
        return groupDao.getAsMap();
    }
}
