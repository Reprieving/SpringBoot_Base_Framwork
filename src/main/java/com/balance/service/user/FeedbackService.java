package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.user.Feedback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 建议反馈
 * Created by weihuaguo on 2018/12/23 18:49.
 */
@Service
public class FeedbackService extends BaseService {

    /**
     * 用户 提交 建议反馈
     */
    public void add(Feedback feedback) {
        Integer type = feedback.getType();
        if (type == null || (type != 1 && type != 2) || StringUtils.isBlank(feedback.getContent())) {
            throw new BusinessException("缺少必要参数");
        }
        insertIfNotNull(feedback);
    }
}
