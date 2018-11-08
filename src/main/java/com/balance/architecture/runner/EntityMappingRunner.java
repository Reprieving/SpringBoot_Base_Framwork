package com.balance.architecture.runner;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Table;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public class EntityMappingRunner implements ApplicationRunner {

    @Resource
    private DataSource dataSource;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void run(ApplicationArguments arguments) {
        try {
            Map<String, Object> entityMap = this.applicationContext.getBeansWithAnnotation(com.balance.architecture.mybatis.annotation.Table.class);
            Connection connection = dataSource.getConnection();
            DatabaseMetaData meta = connection.getMetaData();
            for (Map.Entry<String, Object> entry : entityMap.entrySet()) {
                Class<? extends Object> clazz = entry.getValue()
                        .getClass();
                Table table = clazz.getAnnotation(Table.class);
                ResultSet rs = meta.getTables(null, null, table.name(), new String[]{"TABLE"});
                boolean tableExistFlag = false;
                while (rs.next()) {
                    tableExistFlag = true;
                }
                if (!tableExistFlag) {
                    throw new RuntimeException(table + " is not exist.");
                }
                Field[] fields = clazz.getDeclaredFields();
                SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from " + table.name() + " limit 0");
                SqlRowSetMetaData metaData = rowSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        boolean columnExistFlag = false;
                        Column column = field.getAnnotation(Column.class);
                        for (int i = 1; i <= columnCount; i++) {
                            if (column.name()
                                    .equalsIgnoreCase(metaData.getColumnName(i))) {
                                columnExistFlag = true;
                                break;
                            }
                        }
                        if (!columnExistFlag) {
                            throw new RuntimeException("table[" + table.name() + "]'s column[" + column.name() + "] doesn't exist.");
                        }
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
