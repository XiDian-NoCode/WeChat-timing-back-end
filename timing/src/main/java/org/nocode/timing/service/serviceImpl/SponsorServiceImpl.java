package org.nocode.timing.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nocode.timing.mapper.ActivityMapper;
import org.nocode.timing.mapper.UserActivityMapper;
import org.nocode.timing.mapper.UserMapper;
import org.nocode.timing.pojo.Activity;
import org.nocode.timing.pojo.User;
import org.nocode.timing.pojo.UserActivity;
import org.nocode.timing.service.SponsorService;
import org.nocode.timing.util.SendTemplateMessageUtil;
import org.nocode.timing.util.TemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sponsorServiceImpl")
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
     * 调用userActivityMapper的 List<UserActivity> queryUserActivity(UserActivity userActivity)
     * 根据 `activityId`=activityId + `is_join`=1 + `state`=2 查询到 分表 中的 数据列表
     * 然后是后端逻辑代码处理:对活动列表遍历,获取`user_busy_time`,根据index定位到具体的,看它是否为 '1',是的话就保存到 user列表
     * 然后根据 userId 获取到指定的用户
     */
    @Override
    public List<User> checkMember(int activityId, String sponsorId, int index, int num) {
        UserActivity userActivity = new UserActivity();
        userActivity.setActivityId(activityId);
        byte state = 1;
        userActivity.setState(state);
        userActivity.setIsJoin(true);
        List<UserActivity> userActivityList = userActivityMapper.queryUserActivity(userActivity);
        List<User> userList = new ArrayList<>();
        for (UserActivity u_activity : userActivityList) {
            checkBusyTime(u_activity.getUserBusyTime(), u_activity.getUserId(), index, userList);
        }
        return userList;
    }

    /**
     * @param activityId:总表活动id
     * @param activityTime:所选活动时间的单元格,可以多个
     * @return 流程:
     * 根据 `activityId` 在总表中 更新 `activity_time` 和 `activity_state`=1
     * 然后在 分表 中,根据 `activity_id` + `is_join`=1 更新其 `state`=2
     * 根据 `activity_id` + `is_join`=0 更新其 `state`=3
     */
    @Override
    @Transactional
    public String commitFinalTime(int activityId, String activityTime) {
        // 更新总表
        Activity activity = new Activity();
        activity.setActivityId(activityId);
        activity.setActivityTime(activityTime);
        byte state = 1;
        activity.setActivityState(state);
        int effectNum = activityMapper.updateActivityTimeByActivityId(activity);
        if (effectNum <= 0) {
            return "error";
        } else {
            // 更新参与者分表:根据 `activity_id` + `is_join`=1 更新其 `state`=2
            updateJoinMember(activityId);
            // 更新未参与者分表:根据 `activity_id` + `is_join`=0 更新其 `state`=3
            updateUnJoinMember(activityId);
            return "success";
        }
    }

    /**
     * @param activityId
     * @return 流程:
     * service层:
     * 根据 activityId 查询 分表 中的数据,还要求 `is_join`=1, `state`=2
     * 然后 根据 userId 获取到指定的用户
     * 最后需要调用微信服务器，向所有参与者推送模板消息
     */
    @Override
    @Transactional
    public String informMember(int activityId) throws IOException {
        UserActivity userActivity = new UserActivity();
        userActivity.setActivityId(activityId);
        userActivity.setIsJoin(true);
        byte state = 2;
        userActivity.setState(state);
        // 会有多个
        List<UserActivity> userActivityList = userActivityMapper.queryUserActivity(userActivity);
        if (userActivityList.size() <= 0) {
            return null;
        }
        List<User> userList = new ArrayList<>();
        for (UserActivity u : userActivityList) {
            // 获取所有的参与者
            allJoinUser(u.getUserId(), userList);
        }
        // ================================================================此处增加微信小程序推送消息通知:2019-04-24
        // 获取活动信息
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        // 构造模板消息
        Map<String, TemplateData> map = new HashMap<>();
        map.put("keyword1", new TemplateData(activity.getActivityName()));
        map.put("keyword2", new TemplateData(activity.getActivityTime()));
        map.put("keyword3", new TemplateData(activity.getActivityMembers().toString()));
        map.put("keyword4", new TemplateData(activity.getSponsorName()));
        // 首先获取access_token，有效期两个小时
        String accessToken = SendTemplateMessageUtil.getAccessToken().replaceAll("\"", "");
        // 向所有参与成员推送消息通知
        int count = 0;
        ObjectMapper mapper = new ObjectMapper();
        for (User u : userList) {
            JsonNode js = SendTemplateMessageUtil.sendTemplateMessage(accessToken, u.getUserId(), "5oTlrXvVw0TL-CsBI8AjeoH_XBEhsj3eG_o7eLB1bGY", "/pages/index/index", u.getUserFormId(), map);
            // count 统计发送出成功的人数
            if (mapper.writeValueAsString(js.path("errmsg")).replaceAll("\"", "").equals("ok")) {
                count++;
            }
            System.out.println(js);
        }
        return count + "/" + userList.size();
    }

    private void allJoinUser(String userId, List<User> userList) {
        User user = userMapper.queryUserById(userId);
        userList.add(user);
    }

    // 更新未参与者分表
    private void updateUnJoinMember(int activityId) {
        UserActivity userActivity_unjoin = new UserActivity();
        userActivity_unjoin.setActivityId(activityId);
        userActivity_unjoin.setIsJoin(false);
        byte un_part_state = 3;
        userActivity_unjoin.setState(un_part_state);
        int num = userActivityMapper.updateUserActivity(userActivity_unjoin);
    }

    // 更新参与者分表
    private void updateJoinMember(int activityId) {
        UserActivity userActivity_join = new UserActivity();
        userActivity_join.setActivityId(activityId);
        userActivity_join.setIsJoin(true);
        byte part_state = 2;
        userActivity_join.setState(part_state);
        userActivityMapper.updateUserActivity(userActivity_join);
    }

    /**
     * @param userBusyTime
     * @param index
     * @param userList     根据 userBusyTime 以及 index 获得 具体单元格有事的人
     */
    private void checkBusyTime(String userBusyTime, String userId, int index, List<User> userList) {
        char[] busys = userBusyTime.toCharArray();
        char busy_index = busys[index];
        if (busy_index == '1') {
            User user = userMapper.queryUserById(userId);
            userList.add(user);
        }
    }

}
