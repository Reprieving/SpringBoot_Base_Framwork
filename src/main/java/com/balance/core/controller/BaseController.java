package com.balance.core.controller;

import com.balance.core.dto.Result;
import com.balance.utils.ResultUtils;
import com.balance.work.Exception.DataErrorException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@ControllerAdvice
public class BaseController {

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public Result<?> sqlExceptionHandler(SQLException e) {
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL,e.getMessage());
        return result;
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result<?> arithmeticExceptionHandler(ArithmeticException e) {
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL,e.getMessage());
        return result;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result<?> nullPointerExceptionHandler(NullPointerException e) {
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL,e.getMessage());
        return result;
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseBody
    public Result<?> arrayIndexOutOfBoundsExceptionHandler(ArrayIndexOutOfBoundsException e) {
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL,e.getMessage());
        return result;
    }

    @ExceptionHandler(DataErrorException.class)
    @ResponseBody
    public Result<?> dataErrorExceptionHandler(DataErrorException e) {
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL,e.getMessage());
        return result;
    }
}
