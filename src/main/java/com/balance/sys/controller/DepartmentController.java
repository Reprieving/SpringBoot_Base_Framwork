package com.balance.sys.controller;

import com.balance.core.controller.BaseController;
import com.balance.core.dto.Result;
import com.balance.sys.entity.Department;
import com.balance.utils.ResultUtils;
import com.balance.utils.UUIDUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("department")
@CrossOrigin
public class DepartmentController extends BaseController {

    @RequestMapping("info")
    public Result<?> viewDepartment() throws Exception {
        Department department = new Department();
        department.setId("dcf61ff4a58b4b49804acd9d21be6a0b");
        department.setDepartmentName("13");
        department.setCharger("11");
        department.setCreateBy("1");
        department.setCreateTime(new Timestamp(System.currentTimeMillis()));
        baseSpecs.delete(department);
        return ResultUtils.success(null,"success");
    }

}
