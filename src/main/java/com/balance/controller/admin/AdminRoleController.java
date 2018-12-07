package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.controller.admin.req.RoleReq;
import com.balance.controller.app.req.PaginationReq;
import com.balance.entity.sys.Role;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.sys.RoleFunction;
import com.balance.entity.sys.SubscriberRole;
import com.balance.entity.user.User;
import com.balance.service.sys.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/role")
public class AdminRoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("create")
    public Result<?> createRole(@RequestBody Role role) {
        roleService.insertRole(role);
        return ResultUtils.success("创建角色成功");
    }

    @RequestMapping("list")
    public Result<?> roleList(@RequestBody RoleReq roleReq) {
        Pagination pagination = roleReq.getPagination();
        List<Role> roleList;
        if (StringUtils.isNoneBlank(roleReq.getRoleName())) {
            roleList = roleService.selectListByWhereString(Role.Role_name + " like ", "%" + roleReq.getRoleName() + "%", pagination, Role.class);
        } else {
            roleList = roleService.selectAll(pagination, Role.class);
        }

        Integer count = pagination == null ? roleList.size() : pagination.getTotalRecordNumber();
        return ResultUtils.success(roleList, count);
    }

    @RequestMapping("edit")
    public Result<?> editRole(@RequestBody Role role) {
        Role role1 = roleService.listFunction(role.getId());
        return ResultUtils.success(role1);
    }

}