package com.balance.architecture.mybatis.interceptor;

import com.balance.architecture.mybatis.MybatisMapperParam;
import com.balance.architecture.mybatis.mapper.BaseMapper;
import com.balance.architecture.utils.JDBCResultSetUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Intercepts({
        @Signature(method="handleResultSets", type=ResultSetHandler.class, args={Statement.class})
})
public class BaseResultSetInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(BaseResultSetInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> returnList = new ArrayList<>();

        // 获取代理目标对象
        Object target = invocation.getTarget();
        DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) target;
        // 利用反射获取参数对象
        ParameterHandler parameterHandler = reflectParameterHandler(resultSetHandler);
        MybatisMapperParam parameterObj = (MybatisMapperParam) parameterHandler.getParameterObject();

        Field field = ReflectionUtils.findField(DefaultResultSetHandler.class, "parameterHandler");
        field.setAccessible(true);

        MappedStatement mappedStatement  = reflectMappedStatement(resultSetHandler);
        String statementId = mappedStatement.getId();

        String baseMapperName = new StringBuilder(BaseMapper.class.getSimpleName()).replace(0,1,"b").toString();


        boolean isBaseMapper = false;
        String[] strings = statementId.split("\\.");
        for(String s:strings){
            if(s.equals(baseMapperName)){
                isBaseMapper = true;
            }
        }

        if(!isBaseMapper){
            return invocation.proceed();
        }

        Statement statement = (Statement) invocation.getArgs()[0];
        ResultSet resultSet = statement.getResultSet();

        Class t = parameterObj.getClazz();

        Class entityClass;
        if (t instanceof Class) {
            entityClass = t;
        } else {
            entityClass = t.getClass();
        }

        List<Object> resultSetList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        while (resultSet.next()){
            atomicInteger.addAndGet(1);
            resultSetList.add(JDBCResultSetUtils.getBean(resultSet, entityClass));
        }

        Integer resultSetRows = atomicInteger.intValue();

        Object o;
        if (resultSetRows < 1) {
            return null;
        } else if (resultSetRows == 1){
            o = resultSetList.get(0);
        } else {
            o = resultSetList;
        }

        returnList.add(o);
        return returnList;

    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private ParameterHandler reflectParameterHandler(DefaultResultSetHandler resultSetHandler){
        Field field = ReflectionUtils.findField(DefaultResultSetHandler.class, "parameterHandler");
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(resultSetHandler);
        } catch (Exception e) {
            logger.error("默认返回结果集反射参数对象异常，{}", e.getMessage());
        }
        return (ParameterHandler)value;
    }

    private MappedStatement reflectMappedStatement(DefaultResultSetHandler resultSetHandler){
        Field field = ReflectionUtils.findField(DefaultResultSetHandler.class, "mappedStatement");
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(resultSetHandler);
        } catch (Exception e) {
            logger.error("默认返回结果集反射参数对象异常，{}", e.getMessage());
        }
        return (MappedStatement)value;
    }
}
