package com.balance.entity.common;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("UserInviteCodeId")
@Table(name = "user_invite_code_id")
public class UserInviteCodeId implements Serializable{

    private static final long serialVersionUID = -3355989610316803870L;

    //自增Id
    @Id
    @Column(name = Id)
    private Long id;

    //DB Column name
    public static final String Id = "id";
}
