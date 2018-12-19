package com.balance.service.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.GoodsCategory;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsCategoryService extends BaseService {
    /**
     * 新增类目
     *
     * @param goodsCategory
     */
    public Integer save(GoodsCategory goodsCategory) {
        ValueCheckUtils.notEmpty(goodsCategory.getCategoryName(), "类目不能为空");
        String entityId = goodsCategory.getId();
        if (StringUtils.isNoneBlank(entityId)) {
            ValueCheckUtils.notEmpty(selectOneById(entityId, GoodsCategory.class), "未找到类目记录");
            return updateIfNotNull(goodsCategory);
        } else {
            return insertIfNotNull(goodsCategory);
        }
    }

    /**
     * 类目列表
     *
     * @param goodsCategory 商铺
     * @param pagination    分页对象
     * @return
     */
    public List list(GoodsCategory goodsCategory, Pagination pagination) {
        Class clazz = GoodsCategory.class;
        String categoryName = goodsCategory.getCategoryName();
        Map<String, Object> whereMap;
        if (StringUtils.isNoneBlank(categoryName)) {
            whereMap = ImmutableMap.of(GoodsCategory.Category_name + " LIKE ", "%" + categoryName + "%", GoodsCategory.If_valid + "=", true);
        } else {
            whereMap = ImmutableMap.of(GoodsCategory.If_valid + "=", true);
        }
        return selectListByWhereMap(whereMap, pagination, clazz);
    }


    /**
     * 类目详情
     *
     * @param entityId 实体id
     * @return
     */
    public GoodsCategory detail(String entityId) {
        return selectOneById(entityId, GoodsCategory.class);
    }


    /**
     * 类目操作
     *
     * @param goodsCategory
     * @param operatorType
     * @return
     */
    public Object operatorCategory(GoodsCategory goodsCategory, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case ShopConst.OPERATOR_TYPE_INSERT: //添加
                o = "创建类目成功";
                if (save(goodsCategory) == 0) {
                    throw new BusinessException("创建类目失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_DELETE: //删除
                goodsCategory.setIfValid(false);
                o = "删除类目成功";
                if (updateIfNotNull(goodsCategory) == 0) {
                    throw new BusinessException("删除类目失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = list(goodsCategory, goodsCategory.getPagination());
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = detail(goodsCategory.getId());
                break;
        }
        return o;
    }
}
