package com.balance.entity.shop;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleMachineLocation implements Serializable {
    private static final long serialVersionUID = -7662139180838259860L;

    private String sampleName;
    private String imgUrl;
    private Double coordinateX;
    private Double coordinateY;
    private Integer distance;
    private String cityCode;

    public static final String CityCode = "CityCode";

    public SampleMachineLocation(String sampleName, String imgUrl, Double coordinateX, Double coordinateY, Integer distance) {
        this.sampleName = sampleName;
        this.imgUrl = imgUrl;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.distance = distance;
    }
}
