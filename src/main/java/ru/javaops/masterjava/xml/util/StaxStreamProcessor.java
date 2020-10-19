package ru.javaops.masterjava.xml.util;

import ru.javaops.masterjava.xml.schema.User;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private XMLStreamReader reader;
    private InputStream inputStream;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        inputStream = is;
        reader = FACTORY.createXMLStreamReader(is);
    }

    public XMLStreamReader getReader() {
        return reader;
    }

    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == stopEvent) {
                if (value.equals(getValue(event))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(int event) throws XMLStreamException {
        return (event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? reader.getElementText() : null;
    }

    public List<User> getUsersFromGroup(String groupId) throws XMLStreamException {

        XMLEventReader eventReader= FACTORY.createXMLEventReader(inputStream);
        List<User> users = new ArrayList<>();
        User user = null;
        boolean isGroupIdMatched = false;

        while (eventReader.hasNext()) {
            XMLEvent nextEvent = eventReader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "User":
                        user = new User();
                        isGroupIdMatched = false;
                        Attribute city = startElement.getAttributeByName(new QName("city"));
                        Attribute email = startElement.getAttributeByName(new QName("email"));
                        user.setCity(city.getValue());
                        user.setEmail(email.getValue());
                        break;
                    case "groups":
                        nextEvent = eventReader.nextEvent();
                        isGroupIdMatched = nextEvent.asCharacters().getData().contains(groupId);
                        break;
                    case "fullName":
                        nextEvent = eventReader.nextEvent();
                        user.setFullName(nextEvent.asCharacters().getData());
                        break;
                }
            }

            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("User") && isGroupIdMatched) {
                    users.add(user);
                }
            }
        }

        eventReader.close();
        return users;
    }

    public String getGroupId(String groupName) throws XMLStreamException {
        XMLEventReader eventReader= FACTORY.createXMLEventReader(inputStream);
        Attribute groupId = null;
        while (eventReader.hasNext()) {
            XMLEvent nextEvent = eventReader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("Group")) {
                    groupId = startElement.getAttributeByName(new QName("id"));
                }
                if (startElement.getName().getLocalPart().equals("name")) {
                    nextEvent = eventReader.nextEvent();
                    if (groupName.equals(nextEvent.asCharacters().getData())) {
                        eventReader.close();
                        return groupId != null ? groupId.getValue() : "";
                    }
                }
            }
        }
        eventReader.close();
        return "";
    }

    public String generateHtmlUsersData() throws Exception
    {
        Map<String, String> map = new HashMap<>();
        String email="";
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement()) {
                if (reader.getLocalName().equals("User")) {
                    email=reader.getAttributeValue(null,"email");
                }
                if (reader.getLocalName().equals("fullName")){
                    map.put(reader.getElementText(),email);
                }
            }
        }

        String htmlPage="";

        try (Writer output = new StringWriter()) {
            XMLStreamWriter writer = XMLOutputFactory
                    .newInstance()
                    .createXMLStreamWriter(output);

            writer.writeDTD("<!DOCTYPE html>");
            writer.writeStartElement("html");
            writer.writeAttribute("lang", "en");
            writer.writeStartElement("head");
            writer.writeDTD("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            writer.writeEndElement();

            writer.writeStartElement("body");


            for(Map.Entry<String,String> userWithEmail:map.entrySet())
            {
                writer.writeStartElement("p");
                writer.writeCharacters(userWithEmail.getKey());
                writer.writeEndElement();

                writer.writeStartElement("p");
                writer.writeCharacters(userWithEmail.getValue());
                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();

            htmlPage=output.toString();
        }

        return htmlPage;
    }

    public String getText() throws XMLStreamException {
        return reader.getElementText();
    }

    public void reload(InputStream is) throws Exception {
        inputStream.close();
        inputStream=is;
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // empty
            }
        }
    }
}
