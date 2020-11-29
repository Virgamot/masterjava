package ru.javaops.masterjava.upload.xml.util;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Marshalling/Unmarshalling JAXB helper
 * XML Facade
 */
public class JaxbParser {

    protected JaxbMarshaller jaxbMarshaller;
    protected JaxbUnmarshaller jaxbUnmarshaller;
    protected Schema schema;

    private static List<Class> classes=new ArrayList<>();
    private static JAXBContext jaxbContext;

    public JaxbParser(Class... classesToBeBound) {
        List<Class> classList=Arrays.asList(classesToBeBound);

        if (!classes.containsAll(classList)) {
            classes.addAll(classList);
            try {
                init(getJaxbContext(classes.toArray(new Class[0])));
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static JAXBContext getJaxbContext(Class... classesToBeBound) {
        if (jaxbContext == null) {
            try {
                return JAXBContext.newInstance(classesToBeBound);
            } catch (JAXBException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return jaxbContext;
    }

    public static <T> T safe(Callable<T> fn) {
        try {
            return fn.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    http://stackoverflow.com/questions/30643802/what-is-jaxbcontext-newinstancestring-contextpath
    public JaxbParser(String context) {
        try {
            init(JAXBContext.newInstance(context));
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void init(JAXBContext ctx) throws JAXBException {
        jaxbMarshaller = new JaxbMarshaller(ctx);
        jaxbUnmarshaller = new JaxbUnmarshaller(ctx);
    }

    // Unmarshaller
    public <T> T unmarshal(InputStream is) throws JAXBException {
        return (T) jaxbUnmarshaller.unmarshal(is);
    }

    public <T> T unmarshal(Reader reader) throws JAXBException {
        return (T) jaxbUnmarshaller.unmarshal(reader);
    }

    public <T> T unmarshal(String str) throws JAXBException {
        return (T) jaxbUnmarshaller.unmarshal(str);
    }

    public <T> T unmarshal(XMLStreamReader reader, Class<T> elementClass) throws JAXBException {
        return jaxbUnmarshaller.unmarshal(reader, elementClass);
    }

    // Marshaller
    public void setMarshallerProperty(String prop, Object value) {
        try {
            jaxbMarshaller.setProperty(prop, value);
        } catch (PropertyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String marshal(Object instance) throws JAXBException {
        return jaxbMarshaller.marshal(instance);
    }

    public void marshal(Object instance, Writer writer) throws JAXBException {
        jaxbMarshaller.marshal(instance, writer);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
        jaxbUnmarshaller.setSchema(schema);
        jaxbMarshaller.setSchema(schema);
    }

    public void validate(String str) throws IOException, SAXException {
        validate(new StringReader(str));
    }

    public void validate(Reader reader) throws IOException, SAXException {
        schema.newValidator().validate(new StreamSource(reader));
    }
}
