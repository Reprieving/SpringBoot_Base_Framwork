package com.balance.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.constance.RedisKeyConst;
import com.balance.service.common.AddressService;
import com.balance.service.common.AppUpgradeService;
import com.balance.service.user.ThirdPartyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("app/common")
public class AppCommonController {

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private AppUpgradeService appUpgradeService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 增加分享次数
     *
     * @return
     */
    @RequestMapping("share/increase")
    public Result<?> increaseShareCount(HttpServletRequest request) {
        thirdPartyService.shareCountIncrease(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        return ResultUtils.success();
    }

    /**
     * 获取资源数据
     */
    @GetMapping("resource/{key}")
    public Result<?> resource(@PathVariable String key) {
        Object common = stringRedisTemplate.opsForHash().get(RedisKeyConst.COMMON_RESOURCE, key);
        try {
            return ResultUtils.success(JSONObject.parse(String.valueOf(common)));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtils.success(common);
    }

    /**
     * app 检查版本
     */
    @RequestMapping("checkVer")
    public Result<?> checkVersion(String device, String version) {
        if (StringUtils.isBlank(device) || StringUtils.isBlank(version)) {
            return ResultUtils.error("缺少必要参数");
        }
        return ResultUtils.success(appUpgradeService.checkVersion(device, version));
    }


    /**
     * 获取省市联动数据
     * @param pid
     * @return
     */
    @GetMapping("area/{pid}")
    public Result<?> area(@PathVariable String pid) {
        return ResultUtils.success(addressService.getByPid(pid));
    }


}
