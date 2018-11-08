package com.balance.controller.admin.sys;

import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Role;
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
