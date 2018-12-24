package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.SignInfo;
import com.balance.entity.user.User;
import com.balance.service.mission.MissionService;
import com.balance.service.mission.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping(value = "/app/mission")
@RestController
public class AppMissionController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private SignInService signInService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<?> list() throws UnsupportedEncodingException {
        List<Mission> missions = missionService.getMissionList();
        return ResultUtils.success(missions);
    }

    @ResponseBody
    @RequestMapping(value = "/reward/{missionCompleteId}", method = RequestMethod.POST)
    public Result<?> reward(HttpServletRequest request, @PathVariable String missionCompleteId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        missionService.obtainMissionReward(missionCompleteId, userId);
        return ResultUtils.success();
    }

    @ResponseBody
    @RequestMapping(value = "/sign/list", method = RequestMethod.POST)
    public Result<?> signList(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        SignInfo signList = signInService.getSignList(userId);
        return ResultUtils.success(signList);
    }

    @ResponseBody
    @RequestMapping(value = "/sign/in", method = RequestMethod.POST)
    public Result<?> signIn(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId =  JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        signInService.signIn(userId);
        return ResultUtils.success();
    }

}
