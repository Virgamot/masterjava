package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.util.List;
import java.util.stream.Collectors;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) {
        Payload payload = null;

        try {
            payload = JAXB_PARSER.unmarshal(
                    Resources.getResource("payload.xml").openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> usersOfProject = payload.getUsers().getUser().stream()
                .filter(u -> u.getGroups().stream().anyMatch(g -> ((Project.Groups.Group) g).getName().equals(args[0])))
                .map(u -> u.getFullName()).collect(Collectors.toList());

        usersOfProject.forEach(System.out::println);
    }
}
