package com.balance.work.controller;

import com.balance.core.dto.Result;
import com.balance.work.entity.Project;
import com.balance.utils.ResultUtils;
import com.balance.work.specs.ProjectSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("project")
@CrossOrigin
public class ProjectController{

    @Autowired
    private ProjectSpecs projectSpecs;

    @RequestMapping("create")
    public Result<?> createProject(Project project) throws SQLException,ArithmeticException {
        try {
            projectSpecs.createProject(project);
            return ResultUtils.success(null,"Create success");
        } catch (SQLException e){
            throw new SQLException("Sql exception");
        } catch (ArithmeticException e){
            throw new ArithmeticException("divisor can't be zero");
        }
    }

    @RequestMapping("subProjects")
    public Result<?> querySubProjects(Project project) throws SQLException {
        try{
            List<Project> projectList = projectSpecs.querySubProjects(project);
            return ResultUtils.success(projectList,"Query Success");
        }catch (SQLException e){
            throw new SQLException("sql exception");
        }
    }
}
