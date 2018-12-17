package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.user.MiningRuler;
import com.balance.service.user.MiningRulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("admin/mining")
public class AdminMiningController {
    @Autowired
    private MiningRulerService miningRulerService;

    /**
     * 挖矿规则信息操作
     *
     * @param miningRuler
     * @param operatorType
     * @return
     */
    @RequestMapping("ruler/operator/{operatorType}")
    public Result<?> operatorRuler(@RequestBody MiningRuler miningRuler, @PathVariable Integer operatorType) throws UnsupportedEncodingException {
        Pagination pagination = miningRuler.getPagination();
        Object object = miningRulerService.operatorCategory(miningRuler, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }
}
