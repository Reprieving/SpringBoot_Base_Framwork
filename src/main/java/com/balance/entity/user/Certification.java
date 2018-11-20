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
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //用户名

    @Column(name = "license_type")
    private String licenseType; //证件类型

    @Column(name = "license_number")
    private String licenseNumber; //证件号码

    @Column(name = "positive_photo_url")
    private String positivePhotoUrl; //证件正面照片

    @Column(name = "reverse_photo_url")
    private String reversePhotoUrl; //证件反面照片

    @Column(name = "handle_photo_url")
    private String handlePhotoUrl; //手持身份证照片

    @Column(name = "create_time")
    private Timestamp createTime; //申请认证时间

    //UserConst.USER_CERT_STATUS_*
    @Column(name = "status")
    private Integer status; //认证状态
}
