package com.balance.mapper.sys;

import com.balance.entity.sys.FuncTreeNode;
import com.balance.entity.sys.Function;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionMapper {
    List<FuncTreeNode> selectFuncList(@Param("subscriberId")String subscriberId);

    void insertFuc(@Param("function")Function function);

    Function selectFunc(@Param("function")Function function);
}
