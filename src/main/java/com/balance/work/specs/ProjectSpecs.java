package com.balance.work.specs;

import com.balance.work.entity.Project;

import java.sql.SQLException;
import java.util.List;

public interface ProjectSpecs {
    void createProject(Project project) throws SQLException;

    List<Project> querySubProjects(Project project) throws SQLException;
}
