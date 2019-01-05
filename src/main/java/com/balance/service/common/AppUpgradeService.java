package com.balance.service.common;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.CommonConst;
import com.balance.entity.common.AppUpgrade;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app更新
 */
@Service
public class AppUpgradeService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 检查更新
     */
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


    /**
     * 后台 保存
     */
    public void save(AppUpgrade appUpgrade) {
        if (appUpgrade.getId() != null) {
            //更新
            if (appUpgrade.isState()) {
                // 仅更新状态
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                        Map<String, Object> whereMap = ImmutableMap.of(AppUpgrade.Device + " = ", appUpgrade.getDevice(), AppUpgrade.State + " = ", true);
                        List<AppUpgrade> appUpgrades = selectListByWhereMap(whereMap, null, AppUpgrade.class);
                        for (AppUpgrade upgrade : appUpgrades) {
                            upgrade.setState(false);
                            update(upgrade);
                        }
                        updateIfNotNull(appUpgrade);
                    }
                });
            } else {
                // 更新状态以外的数据
                updateIfNotNull(appUpgrade);
            }
        } else {
            // 新增
            insertIfNotNull(appUpgrade);
        }
    }
}
