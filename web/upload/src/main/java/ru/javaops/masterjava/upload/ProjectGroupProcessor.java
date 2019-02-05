package ru.javaops.masterjava.upload;

import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
import java.util.Map;

public class ProjectGroupProcessor {

    private GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);

    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException {
        val groups = new HashMap<String, Group>();
        while (processor.startElement("Project", "Projects")) {
            Project project = new Project();
            project.setName(processor.getAttribute("name"));
            project.setDescription(processor.getElementValue("description"));
            projectDao.insert(project);
            while(processor.startElement("Group", "Project")){
                Group group = new Group();
                group.setProjectId(project.getId());
                group.setName(processor.getAttribute("name"));
                group.setType(GroupType.valueOf(processor.getAttribute("type")));
                groupDao.insert(group);
                groups.put(group.getName(), group);
            }
        }
        return groups;
    }
}
