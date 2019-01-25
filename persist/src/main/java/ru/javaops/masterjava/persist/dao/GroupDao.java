package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao{

    public Group insert(Group user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWithId(user);
        }
        return user;
    }

    @SqlUpdate("INSERT INTO groups (id, name, type, project_id) VALUES (:id, :name, :email, :projectId) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Group user);

    @SqlUpdate("INSERT INTO groups (id, name, type, project_id) VALUES (:id, :name, :email, :projectId) ")
    abstract void insertWithId(@BindBean Group user);

    @SqlBatch("INSERT INTO groups (id, name, type, project_id) VALUES (:id, :name, :email, :projectId) ")
    public abstract int[] insertBatch(@BindBean List<Group> users, @BatchChunkSize int chunkSize);

    @Override
    @SqlQuery("TRUNCATE groups")
    public abstract void clean();
}
