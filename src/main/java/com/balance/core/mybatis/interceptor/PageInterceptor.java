package com.balance.core.mybatis.interceptor;


import com.balance.core.constance.MybatisConst;
import com.balance.core.dto.Pagination;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
public class PageInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        Object parameterObject =  boundSql.getParameterObject();
        if(parameterObject instanceof Map){
            Map<?, ?> parameterMap = (Map<?, ?>) boundSql.getParameterObject();
            Set<?> keySet = parameterMap.keySet();
            for (Object o : keySet){
                if(MybatisConst.PAGINATION.equals(o.toString())){
                    Pagination pagination = (Pagination) parameterMap.get(MybatisConst.PAGINATION);
                    //统计sql并修改pagination的总数目
                    pagination.setTotalRecordNumber(999);
                    if(pagination!=null){
                        String pageSql = sql + " limit " + pagination.getStartRow() + "," + pagination.getPageSize();
                        metaObject.setValue("delegate.boundSql.sql", pageSql);
                    }
                    break;
                }
            }
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}
