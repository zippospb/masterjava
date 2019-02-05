package ru.javaops.masterjava.service.mail.persist.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;
import ru.javaops.masterjava.service.mail.persist.MailTestData;
import ru.javaops.masterjava.service.mail.persist.model.MailEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MailDaoTest extends AbstractDaoTest<MailDao> {

    public MailDaoTest() {
        super(MailDao.class);
    }

    @BeforeClass
    public static void init() {
        MailTestData.init();
    }

    @Before
    public void setUp() {
        MailTestData.setUp();
    }

    @Test
    public void getAll() throws Exception {
        List<MailEntity> mails = dao.getWithLimit(10);
        assertEquals(MailTestData.MAILS, mails);
    }
}