package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();

    @SqlUpdate("INSERT INTO cities (ref, name) VALUES (:ref, :name) ON CONFLICT DO NOTHING")
    public abstract void insert(@BindBean City user);

    @SqlBatch("INSERT INTO cities (ref, name) VALUES (:ref, :name) ON CONFLICT DO NOTHING")
    public abstract void batchInsert(@BindBean List<City> cities, @BatchChunkSize int chunkSize);

    @SqlQuery("SELECT * FROM cities ORDER BY ref")
    public abstract List<City> getAll();
}
