package com.balance.entity.shop;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SampleMachineSourceLocations implements Serializable{
    private static final long serialVersionUID = -1743992465443256074L;

    private Integer code;
    private List<SampleMachineInfo> datas;
}
