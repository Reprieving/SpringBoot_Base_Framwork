package com.balance.service.mission;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionComplete;
import com.balance.entity.mission.SignIn;
import com.balance.entity.mission.SignInfo;
import com.balance.mapper.mission.MissionMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SignInService extends BaseService {

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionMapper missionMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 签到
     *
     * @param userId
     */
    public void signIn(String userId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(new Date());

                Integer i = missionMapper.selectCountTodaySign(userId, today);
                if (i > 0) {
                    throw new BusinessException("今天已签到了");
                }

                List<Mission> allMission = selectAll(null, Mission.class);

                //每日签到
                Mission daySignMission = missionService.filterTaskByCode(MissionConst.CODE13, allMission);

                //每日签到奖励
                SignIn newSignIn = new SignIn();
                newSignIn.setUserId(userId);

                insertIfNotNull(newSignIn);

                List<SignIn> signList = selectListByWhereString(SignIn.User_id + " = ", userId, new Pagination(), SignIn.class);

                Integer seriesSignCount = 0;

                SimpleDateFormat format = new SimpleDateFormat("MM.dd");
                Calendar c = Calendar.getInstance();
                Date nowDate = new Date();

                List<SignIn> signListApp = new ArrayList<>(30);
                for (int j = 0; j < 30; j++) {
                    SignIn signInApp = new SignIn();
                    c.setTime(nowDate);
                    c.add(Calendar.DATE, -j);
                    Date d = c.getTime();
                    String day = format.format(d);
                    for (SignIn signIn : signList) {
                        String signDay = format.format(signIn.getSignTime());
                        if (day.equals(signDay)) {
                            signInApp.setHasSign(true);
                            break;
                        }
                    }
                    signInApp.setSignStr(day);
                    signListApp.add(signInApp);
                }

                //计算连续签到次数
                Collections.reverse(signListApp);
                for (SignIn signIn : signListApp) {
                    if (!signIn.getHasSign()) {
                        seriesSignCount = 0;
                        continue;
                    }

                    if (signIn.getHasSign()) {
                        seriesSignCount++;
                    }
                }

                //每日签到
                Map<String, Object> whereMap = ImmutableMap.of("mission_id = ", daySignMission.getId(), "user_id = ", userId);
                MissionComplete dayMissionComplete = selectOneByWhereMap(whereMap, MissionComplete.class);
                if (dayMissionComplete == null) {
                    MissionComplete missionComplete = new MissionComplete(daySignMission.getId(), userId, daySignMission.getRewardValue(), MissionConst.MISSION_COMPLETE_STATE_FINISH);
                    insert(missionComplete);
                } else {
                    MissionComplete missionComplete = new MissionComplete(daySignMission.getId(), userId, MissionConst.MISSION_COMPLETE_STATE_RECEIVE);
                    updateIfNotNull(missionComplete);
                }


                //完成7天连续签到
                Mission weekSignMission = missionService.filterTaskByCode(MissionConst.CODE14, allMission);
                if (seriesSignCount % 7 == 0) {
                    MissionComplete weekMissionComplete = missionMapper.selectCompletion(weekSignMission.getId(), userId);
                    if (weekMissionComplete == null) {
                        MissionComplete missionComplete = new MissionComplete(weekSignMission.getId(), userId, weekSignMission.getRewardValue(), MissionConst.MISSION_COMPLETE_STATE_FINISH);
                        insert(missionComplete);
                    } else {
                        MissionComplete missionComplete = new MissionComplete(weekSignMission.getId(), userId, MissionConst.MISSION_COMPLETE_STATE_RECEIVE);
                        updateIfNotNull(missionComplete);
                    }
                }

                //完成30天连续签到
                Mission monthSignMission = missionService.filterTaskByCode(MissionConst.CODE15, allMission);
                if (seriesSignCount % 30 == 0) {
                    MissionComplete monthMissionComplete = missionMapper.selectCompletion(monthSignMission.getId(), userId);
                    if (monthMissionComplete == null) {
                        MissionComplete missionComplete = new MissionComplete(monthSignMission.getId(), userId, monthSignMission.getRewardValue(), MissionConst.MISSION_COMPLETE_STATE_FINISH);
                        insert(missionComplete);
                    } else {
                        MissionComplete missionComplete = new MissionComplete(monthSignMission.getId(), userId, MissionConst.MISSION_COMPLETE_STATE_RECEIVE);
                        updateIfNotNull(missionComplete);
                    }
                }
            }
        });
    }

    /**
     * 获取签到列表信息
     *
     * @param userId
     * @return
     */
    public SignInfo getSignList(String userId) {
        SignInfo signInfo = new SignInfo();
        List<SignIn> signList = selectListByWhereString("user_id = ", userId, new Pagination(), SignIn.class);

        List<SignIn> signListAppReturn = new ArrayList<>();//用户最近30天签到信息(用于返回移动端)

        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        Calendar c = Calendar.getInstance();
        Date nowDate = new Date();


        //计算当天7天签到日期及用户在这7天中是否有签到
        int signSize;
        int appSignListCount = MissionConst.APP_SIGN_VIEW_COUNT;
        if (signList.size() > appSignListCount) {
            signSize = signList.size();
        } else {
            signSize = appSignListCount;
        }
        for (int i = 0; i > -signSize; i--) {
            SignIn signInApp = new SignIn();
            c.setTime(nowDate);
            c.add(Calendar.DATE, i);
            Date d = c.getTime();
            String day = format.format(d);
            for (SignIn signIn : signList) {
                String signDay = format.format(signIn.getSignTime());
                if (day.equals(signDay)) {
                    signInApp.setHasSign(true);
                }
            }
            signInApp.setSignStr(day);
            signListAppReturn.add(signInApp);
        }

        int seriesSignCount = 0;
        //今天是否已签到标志
        Boolean flag = false;

        SimpleDateFormat formatToday = new SimpleDateFormat("MM.dd");
        String today = formatToday.format(new Date());

        //日期改为升序
        Collections.reverse(signListAppReturn);
        if (signList.size() > appSignListCount) {
            signListAppReturn = signListAppReturn.subList(signList.size() - 7, signList.size());
        }

        //计算连续签到次数
        for (int i = 0; i < signListAppReturn.size(); i++) {
            if (!signListAppReturn.get(i).getHasSign() && i != signListAppReturn.size() - 1) {
                seriesSignCount = 0;
                continue;
            }

            if (signListAppReturn.get(i).getHasSign()) {
                seriesSignCount++;
            }

            if (today.equals(signListAppReturn.get(i).getSignStr())) {
                if (signListAppReturn.get(i).getHasSign()) {
                    flag = true;
                }
            }
        }

        signInfo.setSignList(signListAppReturn);
        signInfo.setHasSignToday(flag);
        signInfo.setSeriesSignCount(seriesSignCount);

        return signInfo;
    }
}
