package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.controller.app.req.LockRepOrderPayReq;
import com.balance.entity.application.LockRepository;
import com.balance.entity.application.LockRepositoryOrder;
import com.balance.entity.user.User;
import com.balance.service.application.LockRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("app/application")
public class AppAppletController {

    @Autowired
    private LockRepositoryService lockRepositoryService;



    /**
     * 锁仓产品列表
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("lockRep/list")
    public Result<?> lockRepList(HttpServletRequest request, @RequestBody Pagination pagination) throws UnsupportedEncodingException {
//        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<LockRepository> lockRepositoryList = lockRepositoryService.listLockRepository(null,pagination);
        return ResultUtils.success(lockRepositoryList);
    }

    /**
     * 购买锁仓产品
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("lockRepOrder/create")
    public Result<?> createLockRepOrder(HttpServletRequest request, @RequestBody LockRepOrderPayReq lockRepOrderPayReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        lockRepositoryService.createOrder(userId,lockRepOrderPayReq.getLockRepId(),lockRepOrderPayReq.getBuyAmount());
        return ResultUtils.success("购买成功");
    }


    /**
     * 锁仓订单
     * @param request
     * @param pagination
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("lockRepOrder/list")
    public Result<?> listLockRepOrder(HttpServletRequest request, @RequestBody Pagination pagination) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<LockRepositoryOrder> lockRepositoryOrders = lockRepositoryService.listLockRepOrder(userId,pagination);
        return ResultUtils.success(lockRepositoryOrders);
    }

    /**
     * 锁仓排行榜
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("lockRepOrder/rank")
    public Result<?> listLockRepOrderRank(HttpServletRequest request) throws UnsupportedEncodingException {
//        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<User> rank = lockRepositoryService.listLockRepOrderRank();
        return ResultUtils.success(rank);
    }
}
