package com.balance.sys.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import javax.management.relation.Role;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Alias("Subscriber")
public class Subscriber implements Serializable{
    private static final long serialVersionUID = -5647904280794995483L;

    //database column
    //tId
    private String id;
    //用户名
    private String userName;
    //真实姓名
    private String realName;
    //密码
    private String password;
    //部门id
    private String departmentId;
    //工号
    private String workNumber;
    //创建人
    private String createBy;
    //创建时间
    private Timestamp createTime;
    //是否有效
    private String isValid = "1";

    //value column
    //角色
    private List<Role> roleList;

}
