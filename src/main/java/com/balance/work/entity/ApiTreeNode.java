package com.balance.work.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Alias("ApiTreeNode")
public class ApiTreeNode implements Serializable{
    private static final long serialVersionUID = -8540398946068313293L;
    private String id;
    private String pid;
    private String label;
    private Boolean isProjectRoot;
    private List<ApiTreeNode> children = new ArrayList<>();
}
