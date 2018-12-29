package com.balance.architecture.controller;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.exception.LoginException;
import com.balance.architecture.utils.ResultUtils;
import com.balance.exception.WeChatPayNotifyException;
import com.balance.utils.MineStringUtils;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@ControllerAdvice
public class BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> exceptionHandler(Exception e) {
        logger.error(getExceptionInfo(e));
        String[] arr = e.getClass().getName().split("\\.");
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL, arr[arr.length - 1]);
//        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL, "System error");
        return result;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result<?> businessExceptionHandler(BusinessException e) {
        logger.error(getExceptionInfo(e));
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL, e.getMessage());
        return result;
    }

    @ExceptionHandler(WeChatPayNotifyException.class)
    @ResponseBody
    public String weChatPayNotifyExceptionHandler(WeChatPayNotifyException e) {
        logger.error(getExceptionInfo(e));
        return ResultUtils.weChatPayFail();
    }

    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public Result<?> loginExceptionHandler(Exception e) {
        Result<?> result = ResultUtils.reLogin("");
        return result;
    }

    private static String getExceptionInfo(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }
}
