package com.balance.architecture.mybatis.mapper;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.EntityLackTableAnnotationException;
import com.balance.architecture.mybatis.MybatisMapperParam;
import com.balance.architecture.mybatis.MybatisBuildUtil;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.utils.UUIDUtils;
import com.balance.utils.ValueCheckUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseMapper {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    public Integer insert(Class clazz, Object object) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String tableName = MybatisBuildUtil.getTableName(clazz);
            List<String> dbColumnList = new ArrayList<>(20);
            List<Object> voAttrList = new ArrayList<>(20);
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                Column column = f.getAnnotation(Column.class);
                Id id = f.getAnnotation(Id.class);
                if (id != null) {
                    f.setAccessible(true);
                    f.set(object, UUIDUtils.createUUID());
                }
                if (column != null) {
                    dbColumnList.add(column.name());
                    f.setAccessible(true);
                    voAttrList.add(f.get(object));
                }
            }

            MybatisMapperParam mybatisMapperParam = new MybatisMapperParam();
            mybatisMapperParam.setTableName(tableName);
            mybatisMapperParam.setDbColumnList(dbColumnList);
            mybatisMapperParam.setVoAttrList(voAttrList);

            return sqlSession.insert("baseMapper.insertOne", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;
    }

    public Integer insertIfNotNull(Class clazz, Object object) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String tableName = MybatisBuildUtil.getTableName(clazz);
            List<String> dbColumnList = new ArrayList<>(20);
            List<Object> voAttrList = new ArrayList<>(20);
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                Column column = f.getAnnotation(Column.class);
                Id id = f.getAnnotation(Id.class);
                if (id != null) {
                    f.setAccessible(true);
                    f.set(object, UUIDUtils.createUUID());
                }
                if (column != null) {
                    f.setAccessible(true);
                    Object attrVal = fields[i].get(object);
                    if (attrVal != null) {
                        dbColumnList.add(column.name());
                        voAttrList.add(f.get(object));
                    }
                }
            }

            MybatisMapperParam mybatisMapperParam = new MybatisMapperParam();
            mybatisMapperParam.setTableName(tableName);
            mybatisMapperParam.setDbColumnList(dbColumnList);
            mybatisMapperParam.setVoAttrList(voAttrList);


            return sqlSession.insert("baseMapper.insertOne", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;
    }

    public Integer delete(Class clazz, Object object) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String tableName = MybatisBuildUtil.getTableName(clazz);
            Field[] fields = object.getClass().getDeclaredFields();
            Object idPoVal = null;
            String idDbColumn = "";
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                Id id_annotation = f.getAnnotation(Id.class);
                if (id_annotation != null) {
                    f.setAccessible(true);
                    Column column_annotation = f.getAnnotation(Column.class);
                    idDbColumn = column_annotation.name();
                    idPoVal = f.get(object);
                }
            }

            ValueCheckUtils.notEmpty(idPoVal, "Entity id value can't be null");

            MybatisMapperParam mybatisMapperParam = new MybatisMapperParam();
            mybatisMapperParam.setTableName(tableName);
            mybatisMapperParam.setIdDbColumn(idDbColumn);
            mybatisMapperParam.setIdPoVal(idPoVal);

            return sqlSession.delete("baseMapper.delete", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;

    }

    public Integer update(Class clazz, Object object) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String tableName = MybatisBuildUtil.getTableName(clazz);
            Map<String, Object> setMap = new HashMap<>();
            String idDbColumn = "";
            Object idPoVal = null;
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                Column column = f.getAnnotation(Column.class);
                Id id_annotation = f.getAnnotation(Id.class);
                if (id_annotation != null) {
                    idDbColumn = column.name();
                    idPoVal = f.get(object);
                }
                if (column != null && id_annotation == null) {
                    setMap.put(column.name(), f.get(object));
                }

            }

            ValueCheckUtils.notEmpty(idPoVal, "Entity value can't be null");

            MybatisMapperParam mybatisMapperParam = new MybatisMapperParam();
            mybatisMapperParam.setTableName(tableName);
            mybatisMapperParam.setUpdateMap(setMap);
            mybatisMapperParam.setIdDbColumn(idDbColumn);
            mybatisMapperParam.setIdPoVal(idPoVal);

            return sqlSession.update("baseMapper.update", mybatisMapperParam);

        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;
    }

    public Integer updateIfNotNull(Class clazz, Object object) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String tableName = MybatisBuildUtil.getTableName(clazz);
            Map<String, Object> setMap = new HashMap<>();
            String idDbColumn = "";
            Object idPoVal = null;
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                Column column = f.getAnnotation(Column.class);
                Id id_annotation = f.getAnnotation(Id.class);
                if (id_annotation != null) {
                    idDbColumn = column.name();
                    idPoVal = f.get(object);
                }
                if (column != null && id_annotation == null) {
                    Object attrVal = f.get(object);
                    if (attrVal != null) {
                        setMap.put(column.name(), f.get(object));
                    }
                }
            }

            ValueCheckUtils.notEmpty(idPoVal, "Entity id value can't be null");

            MybatisMapperParam mybatisMapperParam = new MybatisMapperParam();
            mybatisMapperParam.setTableName(tableName);
            mybatisMapperParam.setUpdateMap(setMap);
            mybatisMapperParam.setIdDbColumn(idDbColumn);
            mybatisMapperParam.setIdPoVal(idPoVal);

            return sqlSession.update("baseMapper.update", mybatisMapperParam);

        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return 0;
    }

    public <T> T selectById(Serializable id, Class<T> clazz) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            MybatisMapperParam mybatisMapperParam = MybatisBuildUtil.buildMapperParam4SelectById(id, clazz);
            return sqlSession.selectOne("baseMapper.selectById", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public <T> T selectOneByWhereMap(Map<String, Object> whereMap, Class<T> clazz) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            MybatisMapperParam mybatisMapperParam = MybatisBuildUtil.buildMapperParam4SelectOne(whereMap, clazz);
            return sqlSession.selectOne("baseMapper.selectByWhere", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public <T> List<T> selectAll(Class<T> clazz, Pagination pagination, Map<String, Object> orderMap) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            MybatisMapperParam mybatisMapperParam = MybatisBuildUtil.buildMapperParam4SelectAll(pagination, clazz, orderMap);
            return sqlSession.selectList("baseMapper.selectByWhere", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    public <T> List<T> selectListByWhere(Map<String, Object> whereMap, Class<T> clazz, Pagination pagination,Map<String, Object> orderMap) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            MybatisMapperParam mybatisMapperParam = MybatisBuildUtil.buildMapperParam4SelectListByWhere(whereMap, pagination, clazz,orderMap);
            return sqlSession.selectList("baseMapper.selectByWhere", mybatisMapperParam);
        } catch (EntityLackTableAnnotationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;

    }

}
