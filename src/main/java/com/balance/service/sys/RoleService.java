package com.balance.service.sys;

import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Role;
import com.balance.mapper.sys.RoleMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService{

    @Autowired
    private RoleMapper roleMapper;

    public Integer deleteFunction(String roleId){
        return roleMapper.deleteFunction(roleId);
    }
}
