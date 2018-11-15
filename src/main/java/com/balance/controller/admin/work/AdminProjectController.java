package com.balance.controller.admin.work;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.work.Project;
import com.balance.service.work.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("admin/project")
@CrossOrigin
public class AdminProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("create")
    public Result<?> createProject(Project project) throws SQLException,ArithmeticException {
        try {
            projectService.createProject(project);
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
            List<Project> projectList = projectService.querySubProjects(project);
            return ResultUtils.success(projectList,"Query Success");
        }catch (SQLException e){
            throw new SQLException("sql exception");
        }
    }
}
