package com.balance.architecture.mybatis;

import com.balance.architecture.dto.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MybatisMapperParam {
    //data table name
    private String tableName;

    //data table primary key name
    private String idDbColumn;

    //data table primary key value
    private Object idPoVal;

    //where sql map --
    private Map<String,Object> whereMap;

    //where sql map --
    private Map<String,Object> orderMap;

    //update data table column name and value
    private Map<String,Object> updateMap;

    //data table column name
    private List<String> dbColumnList;

    //data table column value
    private List<Object> voAttrList;

    //pagination
    private Pagination pagination;

    //return data class
    private Class clazz;



}
