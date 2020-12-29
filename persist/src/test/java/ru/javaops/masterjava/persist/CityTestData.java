package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static String SPB_SHORT_NAME="SPB";
    public static City SPB = new City("Saint-Petersburg",SPB_SHORT_NAME);
    public static City MOSCOW = new City("Moscow","MSC");
    public static City KIEV = new City("Kiev","KIV");
    public static City MINSK = new City("Minsk","MSK");
    public static List<City> FIRST4_CITIES= ImmutableList.of(KIEV,MINSK,MOSCOW,SPB);


    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST4_CITIES.forEach(dao::insert);
        });
    }
}
