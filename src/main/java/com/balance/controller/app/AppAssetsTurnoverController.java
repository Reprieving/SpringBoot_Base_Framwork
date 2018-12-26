package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.user.AssetsTurnover;
import com.balance.service.user.AssetsTurnoverService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("app/assetsTurnover")
public class AppAssetsTurnoverController {

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    /**
     * 按类型查询活动记录
     * @param request
     * @param turnoverType 活动记录类型
     * @param pagination
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("list/{turnoverType}")
    public Result<?> list(HttpServletRequest request, @PathVariable("turnoverType") Integer turnoverType,Pagination pagination) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();

        List<AssetsTurnover> list = assetsTurnoverService.listUserTurnover(userId,turnoverType,pagination);

        return ResultUtils.success(list);
    }

}
