package com.balance.controller.admin.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsSpuReq implements Serializable{
    private static final long serialVersionUID = -1981205737684074925L;

    public String dataStr;
    public Object defaultImg;
    public Object detailImg;
}
