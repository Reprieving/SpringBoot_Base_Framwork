package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.constance.UserConst;
import com.balance.entity.mission.Mission;
import com.balance.entity.user.Certification;
import com.balance.mapper.user.CertificationMapper;
import com.balance.service.common.AliOSSBusiness;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CertificationService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private CertificationMapper certificationMapper;

    /**
     * app端申请实名认证
     *
     * @param userId 用户id
     * @param files  图片
     */
    public void createCert(String userId, MultipartFile[] files) {
        Certification certificationPo = selectOneByWhereString(Certification.User_id + "= ", userId, Certification.class);
        if (UserConst.USER_CERT_STATUS_NONE == certificationPo.getStatus()) {
            throw new BusinessException("该用户处于认证中状态,请勿重复提交");
        }
        if (UserConst.USER_CERT_STATUS_PASS == certificationPo.getStatus()) {
            throw new BusinessException("该用户已通过认证.请勿重复提交");
        }

        Certification certification = new Certification();
        String fileDirectory = DateFormatUtils.format(new Date(), "yyyy-MM-dd|HH");
        for (MultipartFile file : files) {
            switch (file.getName()) {
                case UserConst.APPLY_CERT_PIC_TYPE_FRONT:
                    certification.setPositivePhotoUrl(aliOSSBusiness.uploadSensitivePic(file, fileDirectory));
                    break;
                case UserConst.APPLY_CERT_PIC_TYPE_BACK:
                    certification.setReversePhotoUrl(aliOSSBusiness.uploadSensitivePic(file, fileDirectory));
                    break;
                case UserConst.APPLY_CERT_PIC_TYPE_HANDLER:
                    certification.setHandlePhotoUrl(aliOSSBusiness.uploadSensitivePic(file, fileDirectory));
                    break;
            }
        }

        certification.setUserId(userId);

        Integer i = insertIfNotNull(certification);
        if (i == 0) {
            throw new BusinessException("申请实名认证失败,请稍后再试");
        }
    }


    /**
     * 条件查询实名认证申请列表
     *
     * @return
     */
    public List<Certification> listCertification(Certification certification, Pagination pagination) {
        return certificationMapper.listCertification(certification.getUserName(), certification.getRealName(), certification.getLicenseNumber(), pagination);
    }

    /**
     * 审核实名认证
     *
     * @param certId        实名认证记录id
     * @param vertifyStatus 审核状态
     */
    public void updateCert(String certId, Integer vertifyStatus) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Certification certification = new Certification();
                certification.setId(certId);
                certification.setStatus(vertifyStatus);
                Integer i = updateIfNotNull(certification);
                if (i == 0) {
                    throw new BusinessException("审核失败");
                }
                if (vertifyStatus == UserConst.USER_CERT_STATUS_PASS) {
                    Mission mission = missionService.filterTaskByCode(MissionConst.RELEASE_ARTICLE, missionService.selectAll(null, Mission.class));
                    missionCompleteService.createOrUpdateMissionComplete(certification.getUserId(), mission);
                }
            }
        });
    }


}
