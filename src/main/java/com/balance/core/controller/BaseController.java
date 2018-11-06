package com.balance.core.controller;

import com.balance.core.dto.Result;
import com.balance.sys.service.SubscriberService;
import com.balance.utils.ResultUtils;
import com.balance.work.Exception.DataErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

@ControllerAdvice
public class BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> exceptionHandler(Exception e) {
        logger.error(getExceptionInfo(e));
        String[] arr = e.getClass().getName().split("\\.");
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL, arr[arr.length - 1]);
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
