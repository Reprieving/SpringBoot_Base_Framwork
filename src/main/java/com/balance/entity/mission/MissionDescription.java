package com.balance.entity.mission;

import java.io.Serializable;

/**
 * Created by weihuaguo on 2018/12/27 11:55.
 */
public class MissionDescription implements Serializable {

    private String color;
    private String value;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
