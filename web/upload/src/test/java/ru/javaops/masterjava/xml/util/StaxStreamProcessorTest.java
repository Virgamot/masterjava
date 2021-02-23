package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import lombok.val;
import org.junit.Test;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.xml.schema.ObjectFactory;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;

public class StaxStreamProcessorTest {
    @Test
    public void readCities() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    if ("City".equals(reader.getLocalName())) {
                        System.out.println(reader.getElementText());
                    }
                }
            }
        }
    }

    @Test
    public void readCities2() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String city;
            while ((city = processor.getElementValue("City")) != null) {
                System.out.println(city);
            }
        }
    }

    @Test
    public void readProjects() throws Exception {

        final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
        List<Project> projects=new ArrayList<>();
        val unmarshaller = jaxbParser.createUnmarshaller();

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {

            while (processor.startElement("Project", "Projects")) {
                val projectName = processor.getAttribute("name");
                val description = processor.getElementValue("description");
                projects.add(new Project(projectName,description));
            }
            System.out.println("Projects:"+projects);
        }
    }
}