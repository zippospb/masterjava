package ru.javaops.masterjava.xml.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.javaops.masterjava.dto.UserDto;
import ru.javaops.masterjava.xml.schema.FlagType;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoUtils {

    public static Set<UserDto> getUsersFromXml(String file) throws IOException, XMLStreamException {
        Set<UserDto> users = new HashSet<>();
        try (InputStream in = Files.newInputStream(Paths.get(file));
             StaxStreamProcessor processor = new StaxStreamProcessor(in)) {
            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                users.add(new UserDto(processor.getAttribute("email"),
                        FlagType.fromValue(processor.getAttribute("flag")),
                        processor.getText()));
            }
        }
        return users;
    }
}
