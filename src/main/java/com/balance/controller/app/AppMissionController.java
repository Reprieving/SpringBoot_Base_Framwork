package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.applet.Mission;
import com.balance.entity.applet.MissionComplete;
import com.balance.entity.applet.SignInfo;
import com.balance.service.mission.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = "/app/mission")
@Controller
@CrossOrigin
public class AppMissionController {

    @Autowired
    private MissionService missionService;

    @ResponseBody
    @RequestMapping(value = "/list" ,method = RequestMethod.POST)
    public Result<?> getMissionList(HttpServletRequest request, @RequestBody Mission mission){

        String userId = "";
        List<Mission> missions = missionService.getMissionList(userId,mission.getType());

        return ResultUtils.success("");
    }

    @ResponseBody
    @RequestMapping(value = "/reward" ,method = RequestMethod.POST)
    public Result<?> receiveReward(HttpServletRequest request, @RequestBody Mission mission){

        String userId = "";
        missionService.obtainMissionReward(mission.getMissionCompleteId(),userId);

        return ResultUtils.success("");
    }

    @ResponseBody
    @RequestMapping(value = "/signList" ,method = RequestMethod.POST)
    public Result<?> receiveReward(HttpServletRequest request){

        String userId = "";
        SignInfo signList = missionService.getSignList(userId);

        return ResultUtils.success("");
    }

    @ResponseBody
    @RequestMapping(value = "/signIn" ,method = RequestMethod.POST)
    public Result<?> signIn(HttpServletRequest request){

        String userId = "";
        missionService.signIn(userId);

        return ResultUtils.success("");
    }

}
