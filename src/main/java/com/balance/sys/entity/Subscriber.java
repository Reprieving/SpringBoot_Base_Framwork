package com.balance.sys.entity;

import com.balance.core.mybatis.annotation.Column;
import com.balance.core.mybatis.annotation.Id;
import com.balance.core.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Alias("Subscriber")
@Table(name = "sys_subscriber")
@Component
public class Subscriber implements Serializable{
    private static final long serialVersionUID = -5647904280794995483L;

    //database column
    //tId
    @Id
    @Column(name = "id")
    private String id;

    //用户名
    @Column(name = "user_name")
    private String userName;

    //真实姓名
    @Column(name = "real_name")
    private String realName;

    //密码
    @Column(name = "pass_word")
    private String password;

    //部门id
    @Column(name = "department_id")
    private String departmentId;

    //工号
    @Column(name = "work_number")
    private String workNumber;

    //创建人
    @Column(name = "create_by")
    private String createBy;

    //创建时间
    @Column(name = "create_time")
    private Timestamp createTime;

    //是否有效
    @Column(name = "is_valid")
    private Boolean isValid = true;

    //value column
    //角色
    private List<Role> roleList;

}
