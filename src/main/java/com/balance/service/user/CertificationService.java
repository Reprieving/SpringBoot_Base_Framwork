package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.UserConst;
import com.balance.entity.user.Certification;
import com.balance.service.common.AliOSSBusiness;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class CertificationService extends BaseService{

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    /**
     * app端申请实名认证
     * @param userId 用户id
     * @param files 图片
     */
    public void createCert(String userId, MultipartFile[] files) {
        Certification certification = new Certification();
        String fileDirectory = DateFormatUtils.format(new Date(),"yyyy-MM-dd|HH");
        for(MultipartFile file:files){
            switch (file.getName()){
                case UserConst.APPLY_CERT_PIC_TYPE_FRONT:
                    certification.setPositivePhotoUrl(aliOSSBusiness.uploadSensitivePic(file,fileDirectory));
                    break;
                case UserConst.APPLY_CERT_PIC_TYPE_BACK:
                    certification.setReversePhotoUrl(aliOSSBusiness.uploadSensitivePic(file,fileDirectory));
                    break;
                case UserConst.APPLY_CERT_PIC_TYPE_HANDLER:
                    certification.setHandlePhotoUrl(aliOSSBusiness.uploadSensitivePic(file,fileDirectory));
                    break;
            }
        }

        certification.setUserId(userId);

        Integer i = insertIfNotNull(certification);
        if(i==0){
            throw new BusinessException("申请实名认证失败,请稍后再试");
        }
    }
}
