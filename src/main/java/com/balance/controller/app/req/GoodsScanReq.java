package com.balance.controller.app.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsScanReq implements Serializable {
    private static final long serialVersionUID = 4466359607883026073L;

    private String aisleCode; //小样编码
    private String machineCode; //机器编码
}
