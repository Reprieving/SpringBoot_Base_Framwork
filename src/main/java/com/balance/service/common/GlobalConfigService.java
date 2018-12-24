package com.balance.service.common;

import com.balance.architecture.service.BaseService;
import com.balance.entity.common.GlobalConfig;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bint on 2018/8/14.
 */
@Service
public class GlobalConfigService extends BaseService{

    private static Map<String, String> gCfgCache = new HashMap<>();

    @PostConstruct
    private void init(){
        updateGlobalConfigCache();
    }

    private List<GlobalConfig> getAll(){
        List<GlobalConfig> globalConfigList = selectAll(null,GlobalConfig.class);

        return globalConfigList;
    }

    private void updateGlobalConfigCache() {
        List<GlobalConfig> cfgList = this.getAll();

        if (cfgList != null) {
            Iterator<GlobalConfig> it = cfgList.iterator();
            while (it.hasNext()) {
                GlobalConfig globalConfig = it.next();
                gCfgCache.put(globalConfig.getConfigKey(), globalConfig.getConfigValue());
            }
        }
    }

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    public String get(String key) {
        // 如果缓存数据为空，则初始化
        if (gCfgCache.size() == 0) {
            updateGlobalConfigCache();
        }
        String value = gCfgCache.get(key);

        return value;
    }

    public double getDouble (String key) {
        return Double.parseDouble(get(key));
    }

    public static class Constance {
        //阿里云OSS配置
        public final static String OSS_ACCESS_KEY_ID = "OSS_ACCESS_KEY_ID";
        public final static String OSS_ACCESS_KEY_SECRET = "OSS_ACCESS_KEY_SECRET";
        public final static String OSS_END_POINT = "OSS_END_POINT";
        public final static String OSS_COMMON_BUCKET_NAME = "OSS_COMMON_BUCKET_NAME";
        public final static String OSS_SENSITIVE_BUCKET_NAME = "OSS_SENSITIVE_BUCKET_NAME";

        //系统域名
        public final static String SYSTEM_DOMAIN = "SYSTEM_DOMAIN";

        //网建短信系统配置
        public final static String WJ_SMS_ACCESS_KEY = "WJ_SMS_ACCESS_KEY";
        public final static String WJ_SMS_UID = "WJ_SMS_UID";


        /** 银行卡提现 最低额度 */
        public final static String BANK_WITHDRAW_LOWEST = "BANK_WITHDRAW_LOWEST";
        /** 银行卡提现 最高额度 */
        public final static String BANK_WITHDRAW_HIGHEST = "BANK_WITHDRAW_HIGHEST";
    }

}
