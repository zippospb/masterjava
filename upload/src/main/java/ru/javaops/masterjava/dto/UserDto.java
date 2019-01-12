package ru.javaops.masterjava.dto;

import lombok.*;
import ru.javaops.masterjava.xml.schema.FlagType;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {

    private String name;

    private String email;

    private FlagType flagType;

    public UserDto(String email, FlagType flagType, String name) {
        this.name = name;
        this.email = email;
        this.flagType = flagType;
    }
}
