package com.balance.entity.shop;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleMachineInfo implements Serializable{ //小样机信息
    private static final long serialVersionUID = -1743992465443256074L;

    private String id; //机器id
    private String address; //地址
    private String imgUrl; //地址
    private String name; //名字
    private Double longitude; //经度
    private Double latitude; //纬度
}
