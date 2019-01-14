package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.CityType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class JaxbParserTest {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
    private static final JaxbMarshaller MARSHALLER;
    private static final JaxbUnmarshaller UNMARSHALLER;

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
        MARSHALLER = JAXB_PARSER.creatMarshaller();
        UNMARSHALLER = JAXB_PARSER.createUnmarshaller();
    }

    @Test
    public void testPayload() throws Exception {
//        JaxbParserTest.class.getResourceAsStream("/city.xml")
        Payload payload = UNMARSHALLER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        String strPayload = MARSHALLER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        System.out.println(strPayload);
    }

    @Test
    public void testCity() throws Exception {
        JAXBElement<CityType> cityElement = UNMARSHALLER.unmarshal(
                Resources.getResource("city.xml").openStream());
        CityType city = cityElement.getValue();
        JAXBElement<CityType> cityElement2 =
                new JAXBElement<>(new QName("http://javaops.ru", "City"), CityType.class, city);
        String strCity = MARSHALLER.marshal(cityElement2);
        JAXB_PARSER.validate(strCity);
        System.out.println(strCity);
    }
}