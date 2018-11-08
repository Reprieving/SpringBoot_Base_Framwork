package com.balance.service.work;

import com.balance.utils.UUIDUtils;
import com.balance.entity.work.AppInterface;
import com.balance.entity.work.Project;
import com.balance.mapper.work.AppInterfaceMapper;
import com.balance.mapper.work.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private AppInterfaceMapper appInterfaceMapper;

    public void createProject(Project project) throws SQLException{
        project.setId(UUIDUtils.createUUID());
        projectMapper.insertProject(project);

        AppInterface appInterface = new AppInterface();
        appInterface.setId(UUIDUtils.createUUID());
        appInterface.setProjectsId(project.getId());
        appInterface.setInterfaceName(project.getProjectName());
        appInterface.setIsProjectRoot(true);
        appInterfaceMapper.insertAppInterface(appInterface);
    }

    public List<Project> querySubProjects(Project project) throws SQLException {
        return projectMapper.selectSubProjects(project);
    }
}
