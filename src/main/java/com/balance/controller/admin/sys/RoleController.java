package com.balance.controller.admin.sys;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Role;
import com.balance.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private BaseService baseService;

    @RequestMapping("create")
    public Result<?> createRole(Role role){
        baseService.insert(role);
        return ResultUtils.success(null,"success");
    }

    @RequestMapping("list")
    public Result<?> roleList(Role role, Pagination pagination){
        List<Role> roleList = baseService.selectAll(Role.class,pagination);
        return ResultUtils.success(roleList,"success");
    }

}
