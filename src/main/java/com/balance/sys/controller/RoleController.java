package com.balance.sys.controller;

import com.balance.core.dto.Result;
import com.balance.core.service.BaseService;
import com.balance.sys.entity.Role;
import com.balance.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
@CrossOrigin
public class RoleController {

    @Autowired
    private BaseService baseService;

    @RequestMapping("create")
    public Result<?> createFunc(Role role){
        baseService.insert(role);
        return ResultUtils.success(null,"success");
    }

}
