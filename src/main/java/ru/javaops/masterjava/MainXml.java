package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {

    private final static JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);

    public static void main(String[] args) {
        jaxbParser.setSchema(Schemas.ofClasspath("payload.xsd"));

        try {
            Payload payload = jaxbParser.unmarshal(Resources.getResource("payload.xml").openStream());
            new MainXml().findUsersByProjectName(payload, args[0]).forEach(user ->
                    System.out.println("User :" + user.getFullName()));
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> findUsersByProjectName(Payload payload, String projectName) {
        List<String> ids = payload.getProjects().getProject().stream()
                .filter(project -> project.getName().equals(projectName))
                .map(project -> project.getGroups().getGroup().stream().
                        map(Project.Groups.Group::getId).collect(Collectors.toList()))
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
        return payload.getUsers().getUser().stream()
                .filter(user -> user.getGroup().stream().anyMatch(group -> ids.contains(group.getId())))
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());
    }
}
