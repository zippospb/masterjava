package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {

    @Override
    @SqlUpdate("TRUNCATE projects")
    public abstract void clean();

    public Project insert(Project user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWithId(user);
        }
        return user;
    }

    @SqlUpdate("INSERT INTO projects (name, description) VALUES (:name, :description) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project user);

    @SqlUpdate("INSERT INTO projects (name, description) VALUES (:name, :description) ")
    abstract int insertWithId(@BindBean Project user);

    @SqlUpdate("INSERT INTO projects (name, description) VALUES (:name, :description) ")
    public abstract int[] insertBatch(@BindBean List<Project> users, @BatchChunkSize int chunkSize);
}
