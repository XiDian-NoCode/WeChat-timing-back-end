package org.nocode.timing.service.serviceImpl;

import org.nocode.timing.mapper.ActivityMapper;
import org.nocode.timing.mapper.UserActivityMapper;
import org.nocode.timing.pojo.Activity;
import org.nocode.timing.pojo.UserActivity;
import org.nocode.timing.service.ParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ParticipateServiceImpl implements ParticipateService {

    @Autowired
    private UserActivityMapper userActivityMapper;
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * @param userActivityId
     * @param userBusyTime
     * @return 根据 `userActivityId` 更新 分表 中的 userBusyTime, isJoin = 1, state = 1
     * 然后获取这条数据，由此获取到其 对应的 总表id
     * 然后根据 这条数据的 activityId 来获取总表数据
     * 然后处理(主要是activityMembers+1, totalBusyTime 相应位置加 1)
     * 最后更新到数据库
     */
    @Override
    @Transactional
    public int commitMyTime(int userActivityId, String userBusyTime) {
        // 更新分表数据，提交自己的时间
        UserActivity userActivity = new UserActivity();
        userActivity.setUserActivityId(userActivityId); // 设置分表查询条件
        userActivity.setUserBusyTime(userBusyTime);     // 更新分表时间
        userActivity.setIsJoin(true);                   // 更新分表此人已经参与
        byte state = 1;                                 // 更新分表活动状态为1(提交未确定时间)
        userActivity.setState(state);
        userActivityMapper.updateUserActivityForCommitMyTime(userActivity);
        // 获取更新的这条数据，保存到这个 userActivity
        List<UserActivity> userActivityList = userActivityMapper.queryUserActivity(userActivity);
        if (userActivityList.size() == 0) {
            return 0;
        }
        userActivity = userActivityList.get(0);
        // 根据 userActivity 中的 activityId 在总表中更新数据
        int result = operatorActivityForCommitMyTime(userActivity.getActivityId(), userBusyTime);
        return result;
    }

    private int operatorActivityForCommitMyTime(Integer activityId, String userBusyTime) {
        StringBuffer stringBuffer = new StringBuffer();
        // 获取总表数据
        Activity activity = new Activity();
        activity.setActivityId(activityId); // 设置总表查询条件
        List<Activity> activityList = activityMapper.queryActivity(activity);
        if (activityList.size() == 0) {
            return 0;
        }
        activity = activityList.get(0);
        // 更新参加人数
        int memberNums = activity.getActivityMembers();
        activity.setActivityMembers(memberNums + 1);

        // 处理参与时间表格
        String[] one_busys = userBusyTime.split(",");
        String totalBusyTime = activity.getTotalBusyTime();
        String[] all_busys = new String[one_busys.length];
        if (totalBusyTime != null) {
            all_busys = totalBusyTime.split(",");
        } else {
            Arrays.fill(all_busys, "0");
        }
        for (int i = 0; i < one_busys.length; i++) {
            // 分表位置为 1 ,则总表对应位置 加 1
            if (one_busys[i].equals("1")) {
                Integer allNum = Integer.parseInt(all_busys[i]);
                allNum++;
                all_busys[i] = String.valueOf(allNum);
            }
            // 字符串拼接,最后一个不能加 ,
            if (i == all_busys.length - 1) {
                stringBuffer.append(all_busys[i]);
            } else {
                stringBuffer.append(all_busys[i] + ",");
            }
        }
        totalBusyTime = stringBuffer.toString();
        activity.setTotalBusyTime(totalBusyTime);
        // 更新数据库
        int result = activityMapper.updateActivityTimeByActivityId(activity);
        return result;
    }

}
