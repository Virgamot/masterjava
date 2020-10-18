package ru.javaops.masterjava.xml.util;

import ru.javaops.masterjava.xml.schema.User;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private XMLStreamReader reader;
    private XMLEventReader eventReader;
    private InputStream inputStream;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        inputStream = is;
        reader = FACTORY.createXMLStreamReader(is);
        eventReader = FACTORY.createXMLEventReader(is);
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
        return users;
    }

    public String getGroupId(String groupName) throws XMLStreamException {
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
                        return groupId != null ? groupId.getValue() : "";
                    }
                }
            }
        }
        return "";
    }

    public String getText() throws XMLStreamException {
        return reader.getElementText();
    }

    public void reload(InputStream is) throws Exception {
        inputStream.close();
        reader.close();
        eventReader.close();
        reader = FACTORY.createXMLStreamReader(is);
        eventReader = FACTORY.createXMLEventReader(is);
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

        if (eventReader != null) {
            try {
                eventReader.close();
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
