package com.balance.controller.app.req;

import lombok.Data;

@Data
public class UserLocation {
    private String provinceCode;
    private String cityCode;
    private String regionCode;
    private String streetCode;
    private Double coordinateX;
    private Double coordinateY;
}
