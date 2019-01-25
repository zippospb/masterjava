package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.CityType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CityProcessor {
    private final static JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static CityDao cityDao = DBIProvider.getDao(CityDao.class);

    public void process(final InputStream is) throws XMLStreamException, JAXBException {
        List<City> cities = new ArrayList<>();

        val processor = new StaxStreamProcessor(is);
        val unmarshaller = jaxbParser.createUnmarshaller();

        while(processor.startElement("City", "Cities")) {
            CityType xmlCity = unmarshaller.unmarshal(processor.getReader(), CityType.class);
            cities.add(new City(xmlCity.getId(), xmlCity.getValue()));
        }
        cityDao.batchInsert(cities, cities.size());
    }
}
