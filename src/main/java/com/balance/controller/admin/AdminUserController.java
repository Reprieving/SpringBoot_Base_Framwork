package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ResultUtils;
import com.balance.constance.UserConst;
import com.balance.entity.user.Certification;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.service.user.AssetsTurnoverService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 会员
 */
@RequestMapping("admin/user")
@RestController
public class AdminUserController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;


    /**
     * 会员列表
     * @param pagination
     * @param userName
     * @param phoneNumber
     * @return
     */
    @GetMapping("list")
    public Result<?> list(Pagination pagination, String userName, String phoneNumber) {
        HashMap<String, Object> whereMap = Maps.newHashMap();
        if(StringUtils.isNotBlank(userName)) {
            whereMap.put(User.User_name + " = ", userName);
        }
        if(StringUtils.isNotBlank(phoneNumber)) {
            whereMap.put(User.Phone_number + " = ", phoneNumber);
        }
        List<User> userList = baseService.selectListByWhereMap(whereMap, pagination, User.class);
        return ResultUtils.success(userList, pagination.getTotalRecordNumber());
    }

    /**
     * 更新会员状态
     * @param id
     * @param status
     * @return
     */
    @GetMapping("status/{id}/{status}")
    public Result<?> status(@PathVariable String id, @PathVariable Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        baseService.updateIfNotNull(user);
        return ResultUtils.success();
    }


    /**
     * 实名认证列表
     * @param pagination
     * @param licenseNumber
     * @return
     */
    @GetMapping("certificationList")
    public Result<?> certificationList(Pagination pagination, String licenseNumber) {
        HashMap<String, Object> whereMap = Maps.newHashMap();
        if(StringUtils.isNotBlank(licenseNumber)) {
            whereMap.put(Certification.License_number, licenseNumber);
        }
        List<Certification> certificationList = baseService.selectListByWhereMap(whereMap, pagination, Certification.class);
        return ResultUtils.success(certificationList);
    }

    /**
     * 实名审核
     * @param id
     * @param status
     * @return
     */
    @GetMapping("auth/{id}/{status}")
    public Result<?> auth(@PathVariable String id, @PathVariable Integer status) {
        Certification certification = baseService.selectOneById(id, Certification.class);
        if (certification == null || certification.getStatus() != UserConst.USER_CERT_STATUS_NONE) {
            return ResultUtils.error("数据状态异常");
        }
        Certification updateEntity = new Certification();
        updateEntity.setId(id);
        updateEntity.setStatus(status);
        baseService.updateIfNotNull(updateEntity);
        return ResultUtils.success();
    }

    /**
     * 会员 流水列表
     * @return
     */
    @PostMapping("assetsList")
    public Result<?> assetsList(Pagination pagination, String userName, String phoneNumber) {
        return ResultUtils.success(assetsTurnoverService.getByPage(pagination, userName, phoneNumber),
                pagination.getTotalRecordNumber());
    }

    /**
     * 会员资产详细
     * @param id
     * @return
     */
    @GetMapping("assets/{id}")
    public Result<?> auth(@PathVariable String id) {
        return ResultUtils.success(baseService.selectOneByWhereString(UserAssets.User_id + " = ", id, UserAssets.class));
    }



}
