package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.user.MiningRuler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MiningRulerService extends BaseService {

    /**
     * 所有挖矿规则
     *
     * @return
     */
    public List<MiningRuler> listMiningRuler() {
        return selectAll(null, MiningRuler.class);
    }

    /**
     * 根据算力获取对应的挖矿比例
     *
     * @param computePower
     * @return
     */
    public BigDecimal getMiningRateByComputePower(BigDecimal computePower) {
        List<MiningRuler> miningRulerList = listMiningRuler();
        for (MiningRuler miningRuler : miningRulerList) {
            Double computePowerD = computePower.doubleValue();
            if (computePowerD >= miningRuler.getStartValue().doubleValue() && computePowerD < miningRuler.getEndValue().doubleValue()) {
                return miningRuler.getRewardRate();
            }
        }
        return null;
    }

    /**
     * 新增规则
     *
     * @param miningRuler
     */
    public Integer save(MiningRuler miningRuler) {
        BigDecimal zeroBigDecimal = new BigDecimal(0);
        ValueCheckUtils.notEmpty(miningRuler.getStartValue(), "开始值不能为空");
        ValueCheckUtils.notEmpty(miningRuler.getEndValue(), "结束不能为空");
        ValueCheckUtils.notEmpty(miningRuler.getRewardRate(), "比例不能为空");
        ValueCheckUtils.notEmpty(miningRuler.getRewardType(), "奖励类型不能为空");
        if (miningRuler.getStartValue().compareTo(zeroBigDecimal) == -1 || miningRuler.getStartValue().compareTo(zeroBigDecimal) == 0) {
            throw new BusinessException("开始值不能小于或等于0");
        }
        if (miningRuler.getEndValue().compareTo(zeroBigDecimal) == -1 || miningRuler.getEndValue().compareTo(zeroBigDecimal) == 0) {
            throw new BusinessException("结束值不能小于或等于0");
        }
        if (miningRuler.getRewardRate().compareTo(zeroBigDecimal) == -1 || miningRuler.getRewardRate().compareTo(zeroBigDecimal) == 0) {
            throw new BusinessException("比例不能小于或等于0");
        }
        if (miningRuler.getStartValue().compareTo(miningRuler.getEndValue()) == 0 || miningRuler.getStartValue().compareTo(miningRuler.getEndValue()) == 1) {
            throw new BusinessException("开始值不能大于或等于结束值");
        }
        String entityId = miningRuler.getId();
        if (StringUtils.isNoneBlank(entityId)) {
            ValueCheckUtils.notEmpty(selectOneById(entityId, MiningRuler.class), "未找到规则记录");
            return updateIfNotNull(miningRuler);
        } else {
            return insertIfNotNull(miningRuler);
        }
    }

    /**
     * 规则列表
     *
     * @param miningRuler 商铺
     * @param pagination  分页对象
     * @return
     */
    public List list(MiningRuler miningRuler, Pagination pagination) {
        Class clazz = MiningRuler.class;
        return selectAll(pagination, clazz);
    }

    /**
     * 规则详情
     *
     * @param entityId 实体id
     * @return
     */
    public MiningRuler detail(String entityId) {
        return selectOneById(entityId, MiningRuler.class);
    }

    /**
     * 挖矿规则操作
     *
     * @param miningRuler
     * @param operatorType
     * @return
     */
    public Object operatorCategory(MiningRuler miningRuler, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case ShopConst.OPERATOR_TYPE_INSERT: //添加
                o = "创建规则成功";
                if (save(miningRuler) == 0) {
                    throw new BusinessException("创建规则失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_DELETE: //删除
                o = "删除类目成功";
                if (delete(miningRuler) == 0) {
                    throw new BusinessException("删除类目失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = list(miningRuler, miningRuler.getPagination());
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = detail(miningRuler.getId());
                break;
        }
        return o;
    }

}
