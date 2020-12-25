package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static ru.javaops.masterjava.persist.CityTestData.FIRST4_CITIES;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest(){ super(CityDao.class); }

    @Before
    public void setUp() throws Exception
    {
        CityTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<City> cities = dao.getWithLimit(4);
        Assert.assertEquals(FIRST4_CITIES, cities);
    }
}
