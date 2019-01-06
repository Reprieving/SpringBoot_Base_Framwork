package com.balance.entity.common;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("SystemExceptionRecord")
@Table(name = "sys_exception_record")
public class SystemExceptionRecord implements Serializable{

    private static final long serialVersionUID = 6726765517900222097L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Content)
    private String content; //内容

    @Column(name = If_deal)
    private Boolean ifDeal; //用户名

    //DB Column name
    public static final String Id = "id";
    public static final String Content = "content";
    public static final String If_deal = "if_deal";

    public SystemExceptionRecord(){

    }

    public SystemExceptionRecord(String content) {
        this.content = content;
    }
}
