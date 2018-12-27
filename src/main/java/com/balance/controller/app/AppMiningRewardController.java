package com.balance.controller.app;


import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.user.MiningReward;
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

    /**
     * 查询用户收益列表
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<?> list(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<MiningReward> miningRewards = miningRewardService.listMiningReward(userId);
        return ResultUtils.success(miningRewards);
    }


    /**
     * 领取收益
     * @param request
     * @param miningRewardId
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/obtain/{miningRewardId}", method = RequestMethod.POST)
    public Result<?> list(HttpServletRequest request,@PathVariable String miningRewardId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        miningRewardService.obtainMiningReward(miningRewardId,userId);
        return ResultUtils.success();
    }

    /**
     * 用户可偷取收益列表
     * @param request
     * @param stolenUserId
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "stealMiningRewardList/{stolenUserId}", method = RequestMethod.POST)
    public Result<?> stealMiningRewardList(HttpServletRequest request,@PathVariable String stolenUserId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(miningRewardService.listMiningReward(stolenUserId));
    }


    /**
     * 偷取收益
     * @param request
     * @param miningRewardId 收益id
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "steal/{miningRewardId}", method = RequestMethod.POST)
    public Result<?> nearByUserList(HttpServletRequest request,@PathVariable String miningRewardId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        miningRewardService.stealIh(userId,miningRewardId);
        return ResultUtils.success();
    }

    /**
     * 偷取我的收益用户列表
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "stealUserList", method = RequestMethod.POST)
    public Result<?> stealUserList(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(miningRewardService.listStealMiningRecord(userId));
    }


}
