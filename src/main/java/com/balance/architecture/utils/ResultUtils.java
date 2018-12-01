package com.balance.architecture.utils;

import com.balance.architecture.dto.Result;

public class ResultUtils {
    public static final Integer RSP_SUCCESS = 0;//normal request
    public static final Integer RSP_FAIL = -1;//error request
    public static final Integer RSP_LOGIN = 1;//re-login

    //request success
    public static Result success(){
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        return result;
    }

    //request success
    public static Result success(Object data, String msg){
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    //request success
    public static Result success(Object data, Integer count){
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setCount(count);
        result.setData(data);
        return result;
    }

    //request success
    public static Result success(Object data){
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setData(data);
        return result;
    }

    //request success
    public static Result success(String msg){
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setMessage(msg);
        return result;
    }

    //request fail
    public static Result error(int code,String msg){
        Result result = new Result();
        result.setStateCode(RSP_FAIL);
        result.setMessage(msg);
        return result;
    }

    //request fail
    public static Result error(String msg){
        Result result = new Result();
        result.setStateCode(RSP_FAIL);
        result.setMessage(msg);
        return result;
    }

    //request fail
    public static Result reLogin(String msg){
        Result result = new Result();
        result.setStateCode(RSP_LOGIN);
        result.setMessage(msg);
        return result;
    }
}
