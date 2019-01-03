package com.balance.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.common.GlobalConfig;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionDescription;
import com.balance.entity.mission.MissionReward;
import com.balance.service.common.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置相关
 */
@RequestMapping("admin/config")
@RestController
public class AdminConfigController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 全局配置项 列表
     * @return
     */
    @GetMapping("globalList")
    public Result<?> globalList() {
        List<GlobalConfig> data = baseService.selectAll(null, GlobalConfig.class);
        return ResultUtils.success(data, data.size());
    }

    /**
     * 全局配置项 保存
     * @return
     */
    @PostMapping("globalSave/{operation}")
    public Result<?> globalSave(GlobalConfig globalConfig, @PathVariable String operation) {
        int result;
        if ("update".equals(operation)) {
            result = baseService.updateIfNotNull(globalConfig);
        } else {
            result = baseService.insert(globalConfig);
        }
        if (result > 0) {
            globalConfigService.updateGlobalConfigCache();
            return ResultUtils.success();
        } else {
            return ResultUtils.error("保存失败");
        }
    }

    /**
     * 任务配置项 列表
     * @return
     */
    @GetMapping("missionList")
    public Result<?> missionList() {
        List<Mission> data = baseService.selectAll(null, Mission.class);
        return ResultUtils.success(data, data.size());
    }

    /**
     * 任务配置项 保存
     * @return
     */
    @PostMapping("missionSave/{operation}")
    public Result<?> missionAdd(Mission mission, @PathVariable String operation) {
        int result;
        try {
            JSONObject.parseArray(mission.getReward(), MissionReward.class);
            JSONObject.parseArray(mission.getMemberReward(), MissionReward.class);
            JSONObject.parseArray(mission.getDescription(), MissionDescription.class);
        } catch (Exception e) {
            throw new BusinessException("数据格式有误");
        }
        if ("update".equals(operation)) {
            result = baseService.updateIfNotNull(mission);
        } else {
            result = baseService.insert(mission);
        }
        if (result > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error("保存失败");
        }
    }




}
