package com.balance.sys.mapper;

import com.balance.sys.entity.FuncTreeNode;
import com.balance.sys.entity.Function;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionMapper {
    List<FuncTreeNode> selectFuncList(@Param("subscriberId")String subscriberId);

    void insertFuc(@Param("function")Function function);

    Function selectFunc(@Param("function")Function function);
}
