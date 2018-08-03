package com.balance.utils;

import com.balance.sys.entity.FuncTreeNode;
import com.balance.work.entity.ApiTreeNode;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeUtils {
    public static FuncTreeNode generateFucTreeNode(String roleId, List<FuncTreeNode> list) {
        FuncTreeNode current_node = new FuncTreeNode();
        for(FuncTreeNode item:list){
            if(roleId.equals(item.getId())){
                current_node = item;
                break;
            }
        }

        List<FuncTreeNode> childrenFuncTreeNode = new ArrayList<>();
        for(FuncTreeNode item:list){
            if(roleId.equals(item.getPid())){
                childrenFuncTreeNode.add(item);
            }
        }

        for(FuncTreeNode item : childrenFuncTreeNode){
            FuncTreeNode node = generateFucTreeNode(item.getId(),list);
            current_node.getChildren().add(node);
        }
        return current_node;
    }

    public static ApiTreeNode generateApiTreeNode(String roleId, List<ApiTreeNode> list) {
        ApiTreeNode current_node = new ApiTreeNode();
        for(ApiTreeNode item:list){
            if(roleId.equals(item.getId())){
                current_node = item;
                break;
            }
        }

        List<ApiTreeNode> childrenFuncTreeNode = new ArrayList<>();
        for(ApiTreeNode item:list){
            if(roleId.equals(item.getPid())){
                childrenFuncTreeNode.add(item);
            }
        }

        for(ApiTreeNode item : childrenFuncTreeNode){
            ApiTreeNode node = generateApiTreeNode(item.getId(),list);
            current_node.getChildren().add(node);
        }

        return current_node;
    }
}
