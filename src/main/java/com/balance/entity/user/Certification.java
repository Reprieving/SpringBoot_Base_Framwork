package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("Certification")
@Table(name = "user_certification")
public class Certification implements Serializable { //用户认证
    private static final long serialVersionUID = -3096477540596144940L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = License_type)
    private String licenseType; //证件类型

    @Column(name = License_number)
    private String licenseNumber; //证件号码

    @Column(name = Positive_photo_url)
    private String positivePhotoUrl; //证件正面照片

    @Column(name = Reverse_photo_url)
    private String reversePhotoUrl; //证件反面照片

    @Column(name = Handle_photo_url)
    private String handlePhotoUrl; //手持身份证照片

    @Column(name = Create_time)
    private Timestamp createTime; //申请认证时间

    //UserConst.USER_CERT_STATUS_*
    @Column(name = Status)
    private Integer status; //认证状态

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String License_type = "license_type";
    public static final String License_number = "license_number";
    public static final String Positive_photo_url = "positive_photo_url";
    public static final String Reverse_photo_url = "reverse_photo_url";
    public static final String Handle_photo_url = "handle_photo_url";
    public static final String Create_time = "create_time";
    public static final String Status = "status";
}
