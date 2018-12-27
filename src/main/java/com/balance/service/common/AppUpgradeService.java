package com.balance.service.common;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.common.AppUpgrade;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class AppUpgradeService extends BaseService {


    public HashMap<String, Object> checkVersion(String device, String version) {
        HashMap<String, Object> whereMap = Maps.newHashMap();
        whereMap.put(AppUpgrade.Device + " = ", device.toLowerCase());
        whereMap.put(AppUpgrade.State + " = ", true);
        AppUpgrade appUpgrade = selectOneByWhereMap(whereMap, AppUpgrade.class);
        if (appUpgrade == null) {
            throw new BusinessException("检查版本信息错误");
        }
        boolean latest = true;
        boolean isCompel = false;
        String latestVer = appUpgrade.getVersion();
        if (!version.equals(latestVer)) {
            latest = false;
            String compelVersion = appUpgrade.getCompelVersion();
            if ("all".equalsIgnoreCase(compelVersion)) {
                isCompel = true;
            } else {
                String[] compels = compelVersion.split(",");
                for (String compel : compels) {
                    if (version.equals(compel)) {
                        isCompel = true;
                        break;
                    }
                }
            }
        }
        whereMap.clear();
        whereMap.put("latest", latest);
        whereMap.put("url", appUpgrade.getUrl());
        whereMap.put("compel", isCompel);
        whereMap.put("log", appUpgrade.getLog());
        whereMap.put("version", latestVer);
        return whereMap;
    }


}
