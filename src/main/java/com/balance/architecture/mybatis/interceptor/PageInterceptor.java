package com.balance.architecture.mybatis.interceptor;


import com.balance.architecture.constance.MybatisConst;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.MybatisMapperParam;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        if(parameterObject instanceof MybatisMapperParam){
            Pagination pagination = ((MybatisMapperParam) parameterObject).getPagination();
            if(pagination!=null){
                String countSql = "select count(*)from (" + sql + ")a";
                Connection connection = (Connection) invocation.getArgs()[0];
                PreparedStatement countStatement = connection.prepareStatement(countSql);
                ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
                parameterHandler.setParameters(countStatement);
                ResultSet rs = countStatement.executeQuery();
                if (rs.next()) {
                    pagination.setTotalRecordNumber(rs.getInt(1));
                }
                String pageSql = sql + " limit " + pagination.getStartRow() + "," + pagination.getPageSize();
                metaObject.setValue("delegate.boundSql.sql", pageSql);
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
