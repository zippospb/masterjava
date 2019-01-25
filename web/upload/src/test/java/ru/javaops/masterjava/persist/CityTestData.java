package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {

    public static City SPB;
    public static City MOW;
    public static City KIV;
    public static City MNSK;
    public static List<City> CITIES;

    public static void init() {
        SPB = new City("spb", "Санкт-Петербург");
        MOW = new City("mow", "Москва");
        KIV = new City("kiv", "Киев");
        MNSK = new City("mnsk", "Минск");
        CITIES = ImmutableList.of(SPB, MOW, KIV, MNSK);
    }

    public static void setup() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            dao.batchInsert(CITIES, CITIES.size());
        });
    }
}
