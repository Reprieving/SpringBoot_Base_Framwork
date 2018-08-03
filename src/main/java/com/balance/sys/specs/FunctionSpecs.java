package com.balance.sys.specs;

import com.balance.sys.entity.FuncTreeNode;
import com.balance.sys.entity.Function;


public interface FunctionSpecs {

    FuncTreeNode queryFuncTree(String subscriberId);

    void createFunc(Function function);

    Function viewFuc(Function function);
}
