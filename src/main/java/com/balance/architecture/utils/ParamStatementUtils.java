package com.balance.architecture.utils;

import com.balance.entity.work.AppInterface;
import com.balance.entity.work.ParamStatement;
import com.balance.utils.ValueCheckUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParamStatementUtils {
    public static String REQUEST_PARAM_STATEMENT = "1";
    public static String RESPONSE_PARAM_STATEMENT = "2";

    /**
     * @param appInterface appInterface entity
     * @return
     */
    public static List<ParamStatement> buildParamStatementList(AppInterface appInterface) {
        ValueCheckUtils.notEmpty(appInterface,"appInterface can'be null");
        List<ParamStatement> paramStatementList = new ArrayList<>();
        paramStatementList.addAll(appInterface.getReqParamStatement()
                .stream()
                .map(ps -> {
                    ps.setReqRspType(REQUEST_PARAM_STATEMENT);
                    ps.setId(UUIDUtils.createUUID());
                    ps.setAppInterfaceId(appInterface.getId());
                    return ps;
                })
                .collect(Collectors.toList()));
        paramStatementList.addAll(appInterface.getRspParamStatement()
                .stream()
                .map(ps -> {
                    ps.setReqRspType(RESPONSE_PARAM_STATEMENT);
                    ps.setId(UUIDUtils.createUUID());
                    ps.setAppInterfaceId(appInterface.getId());
                    return ps;
                })
                .collect(Collectors.toList()));
        return paramStatementList;
    }


    /**
     * @param appInterface appInterface entity
     * @return
     */
    public static void filterReqRspParamStatement(AppInterface appInterface) {
        ValueCheckUtils.notEmpty(appInterface,"appInterface can'be null");
        appInterface.setReqParamStatement(appInterface.getReqParamStatement()
                .stream()
                .filter(f -> REQUEST_PARAM_STATEMENT.equals(f.getReqRspType()))
                .collect(Collectors.toList()));
        appInterface.setRspParamStatement(appInterface.getRspParamStatement()
                .stream()
                .filter(f -> RESPONSE_PARAM_STATEMENT.equals(f.getReqRspType()))
                .collect(Collectors.toList()));
    }
}
