package ru.javaops.masterjava.upload.xml.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.validation.Schema;
import java.io.StringWriter;
import java.io.Writer;

import static ru.javaops.masterjava.upload.xml.util.JaxbParser.safe;

public class JaxbMarshaller {
    private ThreadLocal<Marshaller> marshaller;

    public JaxbMarshaller(JAXBContext ctx) throws JAXBException {
        marshaller = ThreadLocal.withInitial(()->safe(ctx::createMarshaller));
        marshaller.get().setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.get().setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.get().setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    public void setProperty(String prop, Object value) throws PropertyException {
        marshaller.get().setProperty(prop, value);
    }

    public void setSchema(Schema schema) {
        marshaller.get().setSchema(schema);
    }

    public String marshal(Object instance) throws JAXBException {
        StringWriter sw = new StringWriter();
        marshal(instance, sw);
        return sw.toString();
    }

    public void marshal(Object instance, Writer writer) throws JAXBException {
        marshaller.get().marshal(instance, writer);
    }

}
