package com.balance.controller.app;


import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.mission.Mission;
import com.balance.entity.user.MiningReward;
import com.balance.entity.user.User;
import com.balance.service.mission.MissionService;
import com.balance.service.user.MiningRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping(value = "/app/miningReward")
@RestController
public class AppMiningRewardController {
    @Autowired
    private MiningRewardService miningRewardService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<?> list(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<MiningReward> miningRewards = miningRewardService.listMiningReward(userId);
        return ResultUtils.success(miningRewards);
    }


    @ResponseBody
    @RequestMapping(value = "/obtain/{miningRewardId}", method = RequestMethod.POST)
    public Result<?> list(HttpServletRequest request,@PathVariable String miningRewardId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        miningRewardService.obtainMiningReward(miningRewardId,userId);
        return ResultUtils.success();
    }

}
