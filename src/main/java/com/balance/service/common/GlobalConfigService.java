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

    private static Map<String, String> gCfgCache;

    @PostConstruct
    private void init(){
        updateGlobalConfigCache();
    }

    private List<GlobalConfig> getAll(){
        List<GlobalConfig> globalConfigList = selectAll(null,GlobalConfig.class);

        return globalConfigList;
    }

    public void updateGlobalConfigCache() {
        List<GlobalConfig> cfgList = this.getAll();
        if (cfgList != null) {
            gCfgCache = new HashMap<>();
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
    public String get(Enum key) {
        // 如果缓存数据为空，则初始化
        if (gCfgCache == null || gCfgCache.size() == 0) {
            updateGlobalConfigCache();
        }
        String value = gCfgCache.get(key.toString());

        return value;
    }

    public double getDouble (Enum key) {
        return Double.parseDouble(get(key));
    }

    public double getInt (Enum key) {
        return Integer.parseInt(get(key));
    }

    public enum Enum {
        //阿里云OSS配置
        OSS_ACCESS_KEY_ID,
        OSS_ACCESS_KEY_SECRET,
        OSS_END_POINT,
        OSS_COMMON_BUCKET_NAME,
        OSS_SENSITIVE_BUCKET_NAME,

        //系统域名
        SYSTEM_DOMAIN,

        //网建短信系统配置
        WJ_SMS_TOKEN,
        WJ_SMS_SIGN,

        //微信支付APPID
        WECHAT_PAY_APP_ID,
        //微信商户Id
        WECHAT_MCH_ID,

        /** 银行卡提现 最低额度 */
        BANK_WITHDRAW_LOWEST,
        /** 银行卡提现 最高额度 */
        BANK_WITHDRAW_HIGHEST,


        /** app微信appID */
        APP_WEIXIN_APP_ID,
        /** app微信appSecret */
        APP_WEIXIN_APP_SECRET,

        /** 每天分享最多次数 */
        DAILY_SHARE_TIME,

        //年卡会员办理费用
        MEMBER_FREE
    }

}
