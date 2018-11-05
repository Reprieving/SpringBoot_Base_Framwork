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

import java.sql.SQLException;

@ControllerAdvice
public class BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> sqlExceptionHandler(Exception e) {
        e.printStackTrace();
        String[] arr = e.getClass().getName().split("\\.");
        Result<?> result = ResultUtils.error(ResultUtils.RSP_FAIL,arr[arr.length-1]);
        return result;
    }

}
