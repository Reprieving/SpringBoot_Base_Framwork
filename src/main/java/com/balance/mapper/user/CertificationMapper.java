package com.balance.mapper.user;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.user.Certification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationMapper {

    /**
     * 条件查询实名认证列表
     *
     * @param userName      用户名
     * @param realName      真实姓名
     * @param licenseNumber 证件号码
     * @param pagination    分页对象
     * @return
     */
    List<Certification> listCertification(@Param("userName") String userName, @Param("realName") String realName, @Param("licenseNumber") String licenseNumber, @Param("pagination") Pagination pagination);
}
