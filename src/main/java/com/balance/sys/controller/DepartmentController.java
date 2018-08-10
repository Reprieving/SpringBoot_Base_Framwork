package com.balance.sys.controller;

import com.balance.core.controller.BaseController;
import com.balance.core.dto.Result;
import com.balance.sys.entity.Department;
import com.balance.utils.ResultUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("department")
@CrossOrigin
public class DepartmentController extends BaseController {

    @RequestMapping("info")
    public Result<?> viewDepartment() throws Exception {
        Department department = new Department();
        department.setId("1");
        return ResultUtils.success(baseSpecs.selectById(department),"success");
    }

}
