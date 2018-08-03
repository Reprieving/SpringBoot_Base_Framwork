package com.balance.sys.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Alias("Function")
public class Function implements Serializable{
    private static final long serialVersionUID = 92968053084215365L;

    //database column
    //tId
    private String id;
    //父id
    private String parentId;
    //接口名
    private String functionName;
    //vue组件所属模块
    private String moduleName;
    //url
    private String url;
    //是否菜单项目
    private Boolean isMenu;
    //对应vue组件名
    private String component = "";
    //图标代码
    private String iconCode = "";
    //创建人
    private String createBy = "system";
    //创建时间
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());
    //是否有效
    private Boolean isValid;



}
