package com.balance.constance;

public class CommonConst {
    public final static boolean VALIDITY_FALSE = false;//无效
    public final static boolean VALIDITY_TRUE = true;//无效

    //Mysql 排序
    public final static String ORDER_BY = " ORDER BY ";
    public final static String MYSQL_DESC = " DESC ";
    public final static String MYSQL_ASC = " ASC ";

    //微信用户数据同步开关
    public static Boolean WX_USER_SYNCHRONIZED = true;

    public static final int OPERATOR_TYPE_INSERT = 1;//新增
    public static final int OPERATOR_TYPE_DELETE = 2;//删除
    public static final int OPERATOR_TYPE_UPDATE = 3;//更新
    public static final int OPERATOR_TYPE_QUERY_LIST = 4;//查询列表
    public static final int OPERATOR_TYPE_QUERY_DETAIL = 5;//查询单个

}
