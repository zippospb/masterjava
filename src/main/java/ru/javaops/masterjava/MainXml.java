package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainXml {

    private final static JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);

    public static void main(String[] args) {
        jaxbParser.setSchema(Schemas.ofClasspath("payload.xsd"));

        try {
            Payload payload = jaxbParser.unmarshal(Resources.getResource("payload.xml").openStream());
            MainXml mainXml = new MainXml();
            mainXml.findUsersByProjectNameByJaxb(payload, args[0]).forEach(user ->
                    System.out.println("User :" + user.getFullName()));
            mainXml.findUsersByProjectNameByStax(payload, args[0]).forEach(user ->
                    System.out.println("User :" + user.getFullName() + ", email :" + user.getEmail()));
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> findUsersByProjectNameByJaxb(Payload payload, String projectName) {
        Map<String, String> groupsByProjects = new HashMap<>();
        payload.getProjects().getProject().forEach(
                project -> project.getGroups().getGroup().forEach(
                        group -> groupsByProjects.put(group.getId(), project.getName())));
        return payload.getUsers().getUser().stream()
                .filter(user -> checkGroup(user, groupsByProjects, projectName))
                .collect(Collectors.toList());
    }

    private List<User> findUsersByProjectNameByStax(Payload payload, String projectName) {
        final List<User> users = new ArrayList<>();
        final Map<String, String> groupsByProjects = new HashMap<>();
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                if (reader.next() == XMLEvent.START_ELEMENT) {
                    if (reader.getLocalName().equals("Users")) {
                        users.addAll(parseUsers(reader));
                    } else if ("Projects".equals(reader.getLocalName())) {
                        groupsByProjects.putAll(getProjectGroupsMap(processor));
                    }
                }
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
        return users.stream()
                .filter(user -> checkGroup(user, groupsByProjects, projectName))
                .collect(Collectors.toList());
    }

    private Map<String, String> getProjectGroupsMap(StaxStreamProcessor processor) throws XMLStreamException {
        XMLStreamReader reader = processor.getReader();
        Map<String, String> result = new HashMap<>();
        for (int event = XMLEvent.START_ELEMENT;
             !(event == XMLEvent.END_ELEMENT && "Projects".equals(reader.getLocalName()));
             event = reader.next()) {
            String projectName = processor.getElementValue("name");
            if (projectName == null) {
                return result;
            }
            processor.doUntil(XMLEvent.START_ELEMENT, "Groups");
            result.putAll(getGroupMapByProjectName(projectName, reader));
        }
        return result;
    }

    private Map<String, String> getGroupMapByProjectName(String projectName, XMLStreamReader reader) throws XMLStreamException {
        Map<String, String> result = new HashMap<>();
        for (int event = reader.getEventType();
             !(event == XMLEvent.END_ELEMENT && "Groups".equals(reader.getLocalName()));
             event = reader.next()) {
            if (event == XMLEvent.START_ELEMENT) {
                if ("Group".equals(reader.getLocalName())) {
                    result.put(reader.getAttributeValue(null, "id"), projectName);
                }
            }
        }
        return result;
    }

    private List<User> parseUsers(XMLStreamReader reader) throws XMLStreamException {
        List<User> result = new ArrayList<>();
        for (int event = XMLEvent.START_ELEMENT;
             !(event == XMLEvent.END_ELEMENT && "Users".equals(reader.getLocalName()));
             event = reader.next()) {
            User user;
            if (event == XMLEvent.START_ELEMENT && "User".equals(reader.getLocalName())) {
                result.add(parseUser(reader));
            }
        }
        return result;
    }

    private User parseUser(XMLStreamReader reader) throws XMLStreamException {
        User user = new User();
        user.setEmail(reader.getAttributeValue(null, "email"));
        for (int event = XMLEvent.START_ELEMENT;
             !(event == XMLEvent.END_ELEMENT && "User".equals(reader.getLocalName()));
             event = reader.next()) {
            if (event == XMLEvent.START_ELEMENT) {
                if ("fullName".equals(reader.getLocalName())) {
                    user.setFullName(reader.getElementText());
                } else if ("Group".equals(reader.getLocalName())) {
                    Project.Groups.Group group = new Project.Groups.Group();
                    group.setId(reader.getElementText());
                    user.getGroup().add(new JAXBElement<>(new QName(reader.getNamespaceURI()),
                            Object.class, group));
                }
            }
        }
        return user;
    }

    private boolean checkGroup(User user, Map<String, String> groups, String projectName) {
        return user.getGroup().stream()
                .anyMatch(group -> groups.get(((Project.Groups.Group) group.getValue()).getId()).equals(projectName));
    }
}
