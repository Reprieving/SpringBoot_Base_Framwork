package com.balance.service.sys;

import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Role;
import com.balance.entity.sys.RoleFunction;
import com.balance.mapper.sys.RoleMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleMapper roleMapper;

    public Integer deleteFunction(String roleId) {
        return roleMapper.deleteFunction(roleId);
    }


    public Role listFunction(String roleId) {
        return roleMapper.listFunction(roleId);
    }

    public void insertRole(Role role) {
        Role rolePo = selectOneById(role.getId(), Role.class);
        if (rolePo == null) { //新增
            insertIfNotNull(role);
        } else { //编辑
            deleteFunction(rolePo.getId());
        }
        List<RoleFunction> rolesList = new ArrayList<>(50);
        for (String functionId : role.getFunctionIdList()) {
            rolesList.add(new RoleFunction(functionId, role.getId()));
        }
        insertBatch(rolesList, false);
    }
}
