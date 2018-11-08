package com.balance.utils;

import com.balance.architecture.dto.Result;

public class ResultUtils {
    public static final Integer RSP_SUCCESS = 0;
    public static final Integer RSP_FAIL = -1;
    public static final Integer RSP_LOGIN = 1;

    //request success
    public static Result success(Object data, String msg){
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setMessage(msg);
        result.setObject(data);
        return result;
    }

    //request fail
    public static Result error(int code,String msg){
        Result result = new Result();
        result.setStateCode(RSP_FAIL);
        result.setMessage(msg);
        return result;
    }
}
