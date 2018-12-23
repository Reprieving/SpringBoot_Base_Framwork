package com.balance.entity.common;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

/**
 * APP 更新信息
 * Created by weihuaguo on 2018/12/23 9:44.
 */
@Data
@Alias("AppUpgrade")
@Table(name = "app_upgrade")
public class AppUpgrade {
    @Id
    @Column(name = Id)
    private String id;

    /** 设备 IOS or Adroid*/
    @Column(name = Device)
    private String device;

    /** 版本号 */
    @Column(name = Version)
    private String version;

    /** 下载连接 */
    @Column(name = Url)
    private String url;

    /** 更新日志 */
    @Column(name = Log)
    private String log;

    /** 是否有效 */
    @Column(name = State)
    private boolean state;

    /** 强制更新的版本号 */
    @Column(name = Compel_version)
    private String compelVersion;

    @Column(name = Create_time)
    private Timestamp createTime;

    //DB Column name
    public static final String Id = "id";
    public static final String Device = "device";
    public static final String Version = "version";
    public static final String Url = "url";
    public static final String Log = "log";
    public static final String State = "state";
    public static final String Compel_version = "compel_version";
    public static final String Create_time = "create_time";
}
