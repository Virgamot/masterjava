package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import lombok.val;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.schema.UserWithoutRefs;

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
    public void readUsers() throws Exception {
        JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
        val unmarshaller = jaxbParser.createUnmarshaller();
        List<UserWithoutRefs> users=new ArrayList<>();

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {

            while (processor.doUntil(XMLEvent.START_ELEMENT,"User"))
            {
                UserWithoutRefs user=unmarshaller.unmarshal(processor.getReader(),UserWithoutRefs.class);
                users.add(user);
            }
        }

        users.forEach(System.out::println);
    }
}