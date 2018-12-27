package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.utils.ValueCheckUtils;
import com.balance.constance.MissionConst;
import com.balance.constance.UserConst;
import com.balance.entity.mission.Mission;
import com.balance.entity.user.Certification;
import com.balance.entity.user.InviteUserRecord;
import com.balance.entity.user.User;
import com.balance.mapper.user.CertificationMapper;
import com.balance.service.common.AliOSSBusiness;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    /**
     * app端申请实名认证
     *
     * @param userId        用户id
     * @param realName
     * @param licenseNumber
     * @param frontFiles    图片
     * @param backFiles     图片
     */
    public void createCert(String userId, String realName, String licenseNumber, MultipartFile frontFiles, MultipartFile backFiles) {
        Certification certificationPo = selectOneByWhereString(Certification.User_id + "= ", userId, Certification.class);
        if (certificationPo != null) {
            if (UserConst.USER_CERT_STATUS_ING == certificationPo.getStatus()) {
                throw new BusinessException("该用户处于认证中状态,请勿重复提交");
            }
            if (UserConst.USER_CERT_STATUS_PASS == certificationPo.getStatus()) {
                throw new BusinessException("该用户已通过认证.请勿重复提交");
            }
        }

        ValueCheckUtils.notEmpty(frontFiles, "证件正面图不能为空");
        ValueCheckUtils.notEmpty(frontFiles, "证件背面图不能为空");

        Certification certification = new Certification();
        String fileDirectory = DateFormatUtils.format(new Date(), "yyyy-MM-dd|HH");
        certification.setPositivePhotoUrl(aliOSSBusiness.uploadCommonPic(frontFiles, fileDirectory));
        certification.setReversePhotoUrl(aliOSSBusiness.uploadCommonPic(backFiles, fileDirectory));

        certification.setUserId(userId);
        certification.setRealName(realName);
        certification.setLicenseNumber(licenseNumber);

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
                Certification certificationPo = selectOneById(certId, Certification.class);
                ValueCheckUtils.notEmpty(certificationPo, "未找到申请实名记录");

                Certification certification = new Certification();
                certification.setId(certId);
                certification.setStatus(vertifyStatus);
                Integer i = updateIfNotNull(certification);
                if (i == 0) {
                    throw new BusinessException("审核失败");
                }

                if (vertifyStatus == UserConst.USER_CERT_STATUS_PASS) {
                    String userId = certificationPo.getUserId();
                    User user = selectOneById(userId, User.class);
                    //实名认证
                    missionService.finishMission(user, MissionConst.CERTIFICATION, "实名认证");

                    //增加邀请记录
                    //直接邀请
                    User directInviteUser = selectOneById(user.getInviteId(), User.class);
                    if (directInviteUser != null) {
                        missionService.finishMission(directInviteUser, MissionConst.DIRECT_INVITE, "直接邀请实名认证");
                    }
                    Mission directMission = missionService.filterTaskByCode(MissionConst.DIRECT_INVITE, selectAll(null, Mission.class));//直接邀请任务
                    BigDecimal directRewardValue = directMission.getRewardValue();

                    if (directInviteUser.getMemberType() == UserConst.USER_MEMBER_TYPE_COMMON && directMission.getMemberRewardValue() != null) {
                        directRewardValue = directMission.getMemberRewardValue();
                    }
                    InviteUserRecord directInviteRecord = new InviteUserRecord(userId, directInviteUser.getId(), UserConst.USER_INVITE_TYPE_DIRECT, directRewardValue, directInviteUser.getCreateTime());
                    insertIfNotNull(directInviteRecord);

                    //间接邀请
                    User inDirectInviteUser = selectOneById(directInviteUser.getInviteId(), User.class);
                    if (inDirectInviteUser != null) {
                        missionService.finishMission(inDirectInviteUser, MissionConst.INDIRECT_INVITE, "间接邀请实名认证");
                    }
                    Mission inDirectMission = missionService.filterTaskByCode(MissionConst.DIRECT_INVITE, selectAll(null, Mission.class));//直接邀请任务
                    BigDecimal inDirectRewardValue = inDirectMission.getRewardValue();
                    if (directInviteUser.getMemberType() == UserConst.USER_MEMBER_TYPE_COMMON && inDirectMission.getMemberRewardValue() != null) {
                        inDirectRewardValue = inDirectMission.getMemberRewardValue();
                    }
                    InviteUserRecord inDirectInviteRecord = new InviteUserRecord(userId, inDirectInviteUser.getId(), UserConst.USER_INVITE_TYPE_INDIRECT, inDirectRewardValue, inDirectInviteUser.getCreateTime());
                    insertIfNotNull(inDirectInviteRecord);

                }
            }
        });
    }


}
