package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.user.AssetsTurnover;
import com.balance.entity.user.Certification;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.entity.user.UserMerchantRuler;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.CertificationService;
import com.balance.service.user.UserMerchantService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private UserMerchantService userMerchantRulerService;

    /**
     * 会员列表
     *
     * @param pagination
     * @param userName
     * @param phoneNumber
     * @return
     */
    @GetMapping("list")
    public Result<?> list(Pagination pagination, String userName, String phoneNumber,
                          Integer type, Integer level, Integer sex, Integer memberType, Integer status) {
        HashMap<String, Object> whereMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(userName)) {
            whereMap.put(User.User_name + " = ", userName);
        }
        if (StringUtils.isNotBlank(phoneNumber)) {
            whereMap.put(User.Phone_number + " = ", phoneNumber);
        }
        if (StringUtils.isNotBlank(phoneNumber)) {
            whereMap.put(User.Phone_number + " = ", phoneNumber);
        }
        if (type != null) {
            whereMap.put(User.Type + " = ", type);
        }
        if (sex != null) {
            whereMap.put(User.Sex + " = ", sex);
        }
        if (memberType != null) {
            whereMap.put(User.Member_type + " = ", memberType);
        }
        if (status != null) {
            whereMap.put(User.Status + " = ", status);
        }
        if (level != null) {
            whereMap.put(User.Level + " = ", level);
        }
        List<User> userList = baseService.selectListByWhereMap(whereMap, pagination, User.class);
        return ResultUtils.success(userList, pagination.getTotalRecordNumber());
    }

    /**
     * 更新会员状态
     *
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
     *
     * @param certification
     * @return
     */
    @RequestMapping("cert/list")
    public Result<?> certificationList(@RequestBody Certification certification) {
        Pagination pagination = certification.getPagination();
        List<Certification> certificationList = certificationService.listCertification(certification, pagination);
        return ResultUtils.success(certificationList, pagination.getTotalRecordNumber());
    }

    /**
     * 实名审核
     *
     * @param certId
     * @param status
     * @return
     */
    @RequestMapping("cert/{certId}/{status}")
    public Result<?> auth(@PathVariable String certId, @PathVariable Integer status) {
        certificationService.updateCert(certId, status);
        return ResultUtils.success("审核成功");
    }

    /**
     * 会员 流水列表
     *
     * @return
     */
    @PostMapping("assetsList")
    public Result<?> assetsList(@RequestBody Pagination<AssetsTurnover> pagination) {
        return ResultUtils.success(assetsTurnoverService.getByPage(pagination), pagination.getTotalRecordNumber());
    }

    /**
     * 会员资产详细
     *
     * @param id
     * @return
     */
    @GetMapping("assets/{id}")
    public Result<?> auth(@PathVariable String id) {
        return ResultUtils.success(baseService.selectOneByWhereString(UserAssets.User_id + " = ", id, UserAssets.class));
    }


    /**
     * 商户规则操作
     *
     * @param userMerchantRuler
     * @param operatorType
     * @return
     */
    @RequestMapping("merchantRuler/operator/{operatorType}")
    public Result<?> operatorMerchant(HttpServletRequest request, @RequestBody UserMerchantRuler userMerchantRuler, @PathVariable Integer operatorType) throws UnsupportedEncodingException {
        Pagination pagination = userMerchantRuler.getPagination();
        Object object = userMerchantRulerService.operatorUserMerchantRuler(userMerchantRuler, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }

    /**
     * 更改商户节点
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("userType/update")
    public Result<?> userTypeUpdate(HttpServletRequest request, @RequestBody User user) {
        System.out.println(user);
        return ResultUtils.success("设置商户节点成功");
    }

}
