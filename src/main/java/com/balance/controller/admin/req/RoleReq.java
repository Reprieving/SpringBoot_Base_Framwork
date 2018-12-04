package com.balance.controller.admin.req;

import com.balance.architecture.dto.Pagination;
import lombok.Data;

@Data
public class RoleReq {
    public String roleName;
    public Pagination pagination;
}
