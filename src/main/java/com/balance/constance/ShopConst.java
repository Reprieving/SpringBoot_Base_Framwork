package com.balance.constance;

public class ShopConst {
    //spu类型
    public static final int SPU_TYPE_GOODS = 1;//商城商品
    public static final int SPU_TYPE_BEAUTY = 2;//美妆商品


    //商品状态
    public static final int GOODS_STATUS_OUTSALE = 0;//下架
    public static final int GOODS_STATUS_INSALE = 1;//上架
    public static final int GOODS_STATUS_OUTLINE = -1;//违规


    //商品图片类型
    public static final int GOODS_IMG_TYPE_DEFAULT = 0;//商品默认图
    public static final int GOODS_IMG_TYPE_INTRODUCE = 1;//商品介绍图
    public static final int GOODS_IMG_TYPE_DETAIL = 2;//商品详情图

    //收货地址操作类型
    public static final int SHOPPING_ADDRESS_OPERATOR_TYPE_INSERT = 1;//新增
    public static final int SHOPPING_ADDRESS_OPERATOR_TYPE_DELETE = 2;//删除
    public static final int SHOPPING_ADDRESS_OPERATOR_TYPE_UPDATE = 3;//更新
    public static final int SHOPPING_ADDRESS_OPERATOR_TYPE_QUERY = 4;//查询
    public static final int SHOPPING_ADDRESS_OPERATOR_TYPE_DEFAULT = 5;//设置默认

    //订单状态
    public static final int ORDER_STATUS_NONE = 0;//未发货
    public static final int ORDER_STATUS_CANCEL = -1;//已取消
    public static final int ORDER_STATUS_DELIVERY = 1;//已发货
    public static final int ORDER_STATUS_RECEIVE = 2;//已收货
    public static final int ORDER_STATUS_RETURN_APPLY = 3;//申请退货
    public static final int ORDER_STATUS_RETURN_SUCCESS = 30;//退货成功
    public static final int ORDER_STATUS_RETURN_FAIL= 31;//退货失败
}
