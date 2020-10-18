package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import java.util.List;


public class MainXml {

    public static void main(String[] args) throws Exception {

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            String groupId = processor.getGroupId(args[0]);
            processor.reload(Resources.getResource("payload.xml").openStream());
            List<User> users = processor.getUsersFromGroup(groupId);
            users.forEach(u -> System.out.println(u.getFullName() + " " + u.getEmail()));
        }
    }
}
