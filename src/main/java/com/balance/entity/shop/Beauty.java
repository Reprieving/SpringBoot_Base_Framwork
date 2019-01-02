package com.balance.entity.shop;

import lombok.Data;

import java.io.Serializable;

//小样信息实体
@Data
public class Beauty implements Serializable{

    private static final long serialVersionUID = -4924898777501587875L;

    private String id;
    private String type;
    private String brand;
    private String name;
    private String specification;
    private String price;
    private String member_price;
    private String video_url;
    private String image_url;
    private String image_home;



}
