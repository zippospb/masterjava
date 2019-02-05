package ru.javaops.masterjava.service.mail.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.persist.model.MailEntity;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail CASCADE")
    @Override
    public abstract void clean();

    @SqlUpdate("INSERT INTO mail (list_to, list_cc, subject, body, status, date) " +
            "VALUES (:listTo, :listCc, :subject, :body, :status, :date)")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean MailEntity email);

    @SqlQuery("SELECT * FROM mail ORDER BY date LIMIT :it")
    public abstract List<MailEntity> getWithLimit(@Bind int limit);

    public void insert(MailEntity project) {
        int id = insertGeneratedId(project);
        project.setId(id);
    }
}
