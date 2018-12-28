package com.balance.entity.mission;


import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

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

    @JsonIgnore
    @Column(name = Reward)
    private String reward; //普通用户奖励值 json数组

    @JsonIgnore
    @Column(name = Member_reward)
    private String memberReward; //会员用户奖励值 json数组

    @JsonIgnore
    @Column(name = Description)
    private String description; //描述 json数组

    @Column(name = Action_name)
    private String actionName; //跳转按钮名

    @Column(name = Redirect_type)
    private Integer redirectType; //跳转类型

    @Column(name = If_valid)
    private Boolean ifValid; //是否有效

    @Column(name = Icon)
    private String icon;  //图标链接

    @Column(name = Display)
    private Boolean display;  //图标链接

    @Column(name = Sort)
    private Integer sort;  //排序

    @Column(name = Disposable)
    private Boolean disposable;  //是否是一次性任务


    //---扩展
    private boolean state = true; //状态
    private String missionCompleteId;//任务完成表id
    /**
     * 普通奖励数组
     */
    @JsonIgnore
    private List<MissionReward> rewards;
    /**
     * 会员奖励数组
     */
    @JsonIgnore
    private List<MissionReward> memberRewards;

    /**
     * 任务描述1
     */
    private MissionDescription description1;

    /**
     * 任务描述2
     */
    private MissionDescription description2;

    /**
     * 奖励说明
     */
    private String rewardExplain;

    //DB Column name
    public static final String Id = "id";
    public static final String Task_name = "task_name";
    public static final String Task_code = "task_code";
    public static final String Reward = "reward";
    public static final String Member_reward = "member_reward";
    public static final String Description = "description";
    public static final String Action_name = "action_name";
    public static final String Redirect_type = "redirect_type";
    public static final String If_valid = "if_valid";
    public static final String Icon = "icon";
    public static final String Display = "display";
    public static final String Sort = "sort";
    public static final String Disposable = "disposable";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(Integer taskCode) {
        this.taskCode = taskCode;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getMemberReward() {
        return memberReward;
    }

    public void setMemberReward(String memberReward) {
        this.memberReward = memberReward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }

    public Boolean getIfValid() {
        return ifValid == null ? false : ifValid;
    }

    public void setIfValid(Boolean ifValid) {
        this.ifValid = ifValid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMissionCompleteId() {
        return missionCompleteId;
    }

    public void setMissionCompleteId(String missionCompleteId) {
        this.missionCompleteId = missionCompleteId;
    }

    public List<MissionReward> getRewards() {
        if (StringUtils.isNoneBlank(this.reward)) {
            rewards = JSONObject.parseArray(this.reward, MissionReward.class);
        }
        return rewards;
    }

    public List<MissionReward> getMemberRewards() {
        if (StringUtils.isNoneBlank(this.memberReward)) {
            memberRewards = JSONObject.parseArray(this.memberReward, MissionReward.class);
        }
        return memberRewards;
    }

    public MissionDescription getDescription1() {
        return description1;
    }

    public void setDescription1(MissionDescription description1) {
        this.description1 = description1;
    }

    public MissionDescription getDescription2() {
        return description2;
    }

    public void setDescription2(MissionDescription description2) {
        this.description2 = description2;
    }

    public String getRewardExplain() {
        return rewardExplain;
    }

    public void setRewardExplain(String rewardExplain) {
        this.rewardExplain = rewardExplain;
    }

    public Boolean getDisposable() {
        return disposable == null ? false : disposable;
    }

    public void setDisposable(Boolean disposable) {
        this.disposable = disposable;
    }
}
