package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.SignInfo;
import com.balance.service.mission.MissionService;
import com.balance.service.mission.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = "/app/mission")
@RestController
public class AppMissionController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private SignInService signInService;

    /**
     * 有效的任务列表
     */
    @RequestMapping(value = "/list")
    public Result<?> list(HttpServletRequest request) {
        List<Mission> missions = missionService.getValidList(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        return ResultUtils.success(missions);
    }

    /**
     * 任务完成 领取奖励
     */
    @RequestMapping(value = "/reward/{missionCompleteId}")
    public Result<?> reward(HttpServletRequest request, @PathVariable String missionCompleteId) {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        missionService.obtainMissionReward(missionCompleteId, userId);
        return ResultUtils.success();
    }

    /**
     * 签到列表
     */
    @RequestMapping(value = "/sign/list")
    public Result<?> signList(HttpServletRequest request) {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        SignInfo signList = signInService.getSignList(userId);
        return ResultUtils.success(signList);
    }

    /**
     * 签到
     */
    @RequestMapping(value = "/sign/in")
    public Result<?> signIn(HttpServletRequest request) {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(signInService.signIn(userId));
    }

    /**
     * 分享任务
     */
    @RequestMapping(value = "share")
    public Result<?> share(HttpServletRequest request) {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        missionService.share(userId);
        return ResultUtils.success();
    }

}
