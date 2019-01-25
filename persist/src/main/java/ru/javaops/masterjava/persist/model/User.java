package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    private @NonNull @Column("full_name") String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    private @NonNull @Column("city_ref") String cityRef;

    public User(Integer id, String fullName, String email, UserFlag flag, String cityRef) {
        this(fullName, email, flag, cityRef);
        this.id=id;
    }
}