package org.nocode.timing.service.serviceImpl;

import org.nocode.timing.mapper.ActivityMapper;
import org.nocode.timing.mapper.UserActivityMapper;
import org.nocode.timing.mapper.UserMapper;
import org.nocode.timing.pojo.Activity;
import org.nocode.timing.pojo.User;
import org.nocode.timing.pojo.UserActivity;
import org.nocode.timing.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SponsorServiceImpl implements SponsorService {
    @Autowired
    private UserActivityMapper userActivityMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * @param activityId:总表活动id
     * @param sponsorId:用户id,发起人id
     * @param index:单元格位置
     * @param num:人数
     * @return 返回用户列表给controller
     * 功能:
     *    调用userActivityMapper的 List<UserActivity> queryUserActivity(UserActivity userActivity)
     *    根据 `activityId`=activityId + `is_join`=1 + `state`=2 查询到 分表 中的 数据列表
     *    然后是后端逻辑代码处理:对活动列表遍历,获取`user_busy_time`,根据index定位到具体的,看它是否为 '1',是的话就保存到 user列表
     */
    @Override
    public List<User> checkMember(int activityId, String sponsorId, int index, int num) {
        UserActivity userActivity = new UserActivity();
        userActivity.setActivityId(activityId);
        byte state = 2;
        userActivity.setState(state);
        userActivity.setIsJoin(true);
        List<UserActivity> userActivityList = userActivityMapper.queryUserActivity(userActivity);
        List<User> userList = new ArrayList<>();
        for(UserActivity u_activity:userActivityList) {
            checkBusyTime(u_activity.getUserBusyTime(), u_activity.getUserId(), index, userList);
        }
        return userList;
    }

    /**
     * @param activityId:总表活动id
     * @param activityTime:所选活动时间的单元格,可以多个
     * @return
     *
     */
    @Override
    public String commitFinalTime(int activityId, String activityTime) {
        Activity activity = new Activity();
        activity.setActivityId(activityId);
        activity.setActivityTime(activityTime);
        int i = activityMapper.updateActivityTimeByActivityId(activity);
        if(i==1)
            return "success";
        else
            return "error";
    }

    /**
     * @param userBusyTime
     * @param index
     * @param userList
     * 根据 userBusyTime 以及 index 获得 具体单元格有事的人
     */
    private void checkBusyTime(String userBusyTime, String userId, int index, List<User> userList) {
        String[] busys = userBusyTime.split(",");
        String busy_index = busys[index];
        if(busy_index.equals("1")) {
            User user = userMapper.queryUserById(userId);
            userList.add(user);
        }
    }


}
