package com.balance.mapper.sys;

import com.balance.entity.sys.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper {

    Integer deleteFunction(@Param("roleId") String roleId);

    Role listFunction(@Param("roleId") String roleId);
}
