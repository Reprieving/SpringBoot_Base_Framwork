package com.balance.entity.mission;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("Mission")
@Table(name = "mission")
public class Mission implements Serializable {
    private static final long serialVersionUID = 6479894383573534305L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Task_name)
    private String taskName; //任务名

    @Column(name = Task_code)
    private Integer taskCode; //任务编码

    @Column(name = Settlement_id)
    private Integer settlementId; //任务类型(支付方式)

    @Column(name = Reward_value)
    private BigDecimal rewardValue; //普通用户奖励值

    @Column(name = Member_reward_value)
    private BigDecimal memberRewardValue; //会员用户奖励值

    @Column(name = Disposable)
    private Integer disposable; //是否一次性任务

    @Column(name = Description)
    private String description; //描述

    @Column(name = Action_name)
    private String actionName; //跳转按钮名

    @Column(name = Redirect_type)
    private Integer redirectType; //跳转类型

    @Column(name = Task_type)
    private Integer taskType; //任务类型

    @Column(name = If_valid)
    private Integer ifValid; //是否有效


    //---扩展
    private Integer state; //状态
    private String missionCompleteId;//任务完成表id


    //DB Column name
    public static final String Id = "id";
    public static final String Task_name = "task_name";
    public static final String Task_code = "task_code";
    public static final String Settlement_id = "settlement_id";
    public static final String Reward_value = "reward_value";
    public static final String Member_reward_value = "member_reward_value";
    public static final String Disposable = "disposable";
    public static final String Description = "description";
    public static final String Action_name = "action_name";
    public static final String Redirect_type = "redirect_type";
    public static final String If_valid = "if_valid";
    public static final String Task_type = "task_type";
}
