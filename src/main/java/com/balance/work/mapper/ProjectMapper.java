package com.balance.work.mapper;

import com.balance.work.entity.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMapper {
    void insertProject(@Param("project") Project project);

    List<Project> selectSubProjects(@Param("project") Project project);

}
