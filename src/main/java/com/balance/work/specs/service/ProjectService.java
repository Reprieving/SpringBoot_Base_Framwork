package com.balance.work.specs.service;

import com.balance.utils.UUIDUtils;
import com.balance.work.entity.AppInterface;
import com.balance.work.entity.Project;
import com.balance.work.mapper.AppInterfaceMapper;
import com.balance.work.mapper.ProjectMapper;
import com.balance.work.specs.ProjectSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService implements ProjectSpecs {

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
