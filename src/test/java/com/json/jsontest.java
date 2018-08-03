package com.json;

import com.alibaba.fastjson.JSON;
import com.balance.work.entity.AppInterface;
import com.balance.work.entity.ParamStatement;

import java.util.ArrayList;

public class jsontest {
    public static void main(String[] args) {
//        String s = {"httpType":"GET","url":"weqqeq","interfaceName":"weq","reqParamStatement":"[{\n            \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }, {\n            \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }, {\n             \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }, {\n             \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }]","reqParamContent":"weq","rspParamStatement":"[{\n            \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }, {\n            \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }, {\n             \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }, {\n             \"date\": \"2016-05-02\",\n            \"name\": \"王小虎\",\n            \"address\": \"上海市普陀区金沙江路 1518 弄\"\n          }]","rspParamContent":"we","parentId":"ef34fa0486ff463a8fd0fa66d8694c06","projectId":"817ae663d30d48e2953e65fa04abc873"};
        String s = "{\"isProjectRoot'\":false,\"isValid\":true,\"reqParamStatement\":[],\"rspParamStatement\":[]}";
        String inj_str = "'|and|exec|insert|select|delete|update|" +
                "count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
        System.out.println(s.indexOf("'"));
    }
}
