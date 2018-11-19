package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.GoodsSpu;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("admin/goods")
public class AppGoodsController {

    public Result<?> spuList(HttpServletRequest request, @RequestBody GoodsSpu goodsSpu){




        return ResultUtils.success("");
    }
}
