package ru.javaops.masterjava.service.mail.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.dao.MailDao;
import ru.javaops.masterjava.service.mail.persist.model.MailEntity;

import java.util.Date;
import java.util.List;

public class MailTestData {
    public static MailEntity MAIL_OK;
    public static MailEntity MAIL_ERROR;

    public static List<MailEntity> MAILS;

    public static void init() {
        MAIL_OK = new MailEntity("to1", "cc1", "subject1", "body1", "OK",
                new Date());
        MAIL_ERROR = new MailEntity("to2", "cc2", "subject2", "body2", "ERROR",
                new Date());

        MAILS = ImmutableList.of(MAIL_OK, MAIL_ERROR);
    }

    public static void setUp() {
        MailDao dao = DBIProvider.getDao(MailDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> MAILS.forEach(dao::insert));
    }



}
