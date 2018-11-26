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
    @Column(name = "id")
    private String id;

    @Column(name = "task_name")
    private String taskName; //任务名

    @Column(name = "task_code")
    private Integer taskCode; //任务编码

    @Column(name = "type")
    private Integer type; //任务类型

    @Column(name = "reward_value")
    private BigDecimal rewardValue; //奖励值

    @Column(name = "disposable")
    private Integer disposable; //是否一次性任务

    @Column(name = "description")
    private String description; //描述

    @Column(name = "action_name")
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
    @Column(name = "redirect_type")
    private Integer redirectType; //跳转类型

    @Column(name = "is_valid")
    private Integer isValid; //是否有效


    //---扩展
    private Integer state; //状态
    private String missionCompleteId;//任务完成表id
}
