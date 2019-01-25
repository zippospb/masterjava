package ru.javaops.masterjava.upload;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.DBITestProvider;
import ru.javaops.masterjava.persist.dao.CityDao;

import static org.junit.Assert.*;

public class CityProcessorTest {

    static  {
        DBITestProvider.initDBI();
    }

    private CityDao cityDao = DBIProvider.getDao(CityDao.class);

    @Test
    public void process() throws Exception {
        CityProcessor cityProcessor = new CityProcessor();
        cityProcessor.process(Resources.getResource("payload.xml").openStream());
        assertEquals(4, cityDao.getAll().size());
    }

}