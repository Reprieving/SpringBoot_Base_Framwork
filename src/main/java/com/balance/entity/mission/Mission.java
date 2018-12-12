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
    private BigDecimal rewardValue; //奖励值

    @Column(name = Disposable)
    private Integer disposable; //是否一次性任务

    @Column(name = Description)
    private String description; //描述

    @Column(name = Action_name)
    private String actionName; //跳转按钮名

    /**
     * 1.邀请好友
     * 2.关注公众号
     * 3.购买商品
     * 4.实名认证
     * 5.锁仓
     * 6.邀请好友
     * 7.分享
     * 8.幸运抽奖
     * 9.首次充值
     * 10.新用户注册
     * 11.每日签到
     * 12.每周签到
     * 13.每月签到
     * 14.新人矿石礼包
     */
    @Column(name = Redirect_type)
    private Integer redirectType; //跳转类型

    @Column(name = Task_type)
    private Integer taskType; //任务类型

    @Column(name = Is_valid)
    private Integer isValid; //是否有效


    //---扩展
    private Integer state; //状态
    private String missionCompleteId;//任务完成表id


    //DB Column name
    public static final String Id = "id";
    public static final String Task_name = "task_name";
    public static final String Task_code = "task_code";
    public static final String Settlement_id = "settlement_id";
    public static final String Reward_value = "reward_value";
    public static final String Disposable = "disposable";
    public static final String Description = "description";
    public static final String Action_name = "action_name";
    public static final String Redirect_type = "redirect_type";
    public static final String Is_valid = "is_valid";
    public static final String Task_type = "task_type";
}
