package ru.javaops.masterjava.service.mail.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.service.mail.Addressee;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MailEntity extends BaseEntity {

    private @Column("list_to") String listTo;
    private @Column("list_cc") String listCc;
    private String subject;
    private String body;
    private String status;
    private Date date;

    public static MailEntity of(List<Addressee> to, List<Addressee> cc, String subject, String body, String error) {
        return new MailEntity(String.join(", ", to.stream().map(Object::toString).toArray(String[]::new)),
                String.join(", ", cc.stream().map(Object::toString).toArray(String[]::new)),
                subject,
                body,
                error,
                new Date());
    }
}
