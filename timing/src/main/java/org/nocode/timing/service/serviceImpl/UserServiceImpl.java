package org.nocode.timing.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nocode.timing.mapper.ActivityMapper;
import org.nocode.timing.mapper.UserActivityMapper;
import org.nocode.timing.mapper.UserMapper;
import org.nocode.timing.pojo.Activity;
import org.nocode.timing.pojo.User;
import org.nocode.timing.pojo.UserActivity;
import org.nocode.timing.service.UserService;
import org.nocode.timing.util.AesCbcUtil;
import org.nocode.timing.util.GetOpenIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    public static final String APPID = "wx0885230cd5c5837d";
    public static final String SECRET = "5dabd06fceb7c6cd07bd437fa8c07269";

    private UserMapper userMapper;
    private ActivityMapper activityMapper;
    private UserActivityMapper userActivityMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setActivityMapper(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    @Autowired
    public void setUserActivityMapper(UserActivityMapper userActivityMapper) {
        this.userActivityMapper = userActivityMapper;
    }

    @Override
    public String login(String code, String encryptedData, String iv) throws IOException {

        GetOpenIdUtil getOpenId = new GetOpenIdUtil();
        // 调用访问微信服务器工具方法，传入三个参数获取带有openid、session_key的json字符串
        String jsonId = getOpenId.getOpenId(APPID, code, SECRET);
        System.out.println(jsonId);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonId);
        // 从json字符串获取openid和session_key
        String openid = mapper.writeValueAsString(rootNode.path("openid")).replaceAll("\"", "");
        // 数据库查询用户是否已经存在
        if (userMapper.selectByPrimaryKey(openid) == null) {
            // 如果用户不存在，获取session_key
            String session_key = mapper.writeValueAsString(rootNode.path("session_key"));
            try {
                // 得到用户信息
                String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
                // 存入数据库
                if (null != result && result.length() > 0) {
                    User user = new User();
                    rootNode = mapper.readTree(result);
                    user.setUserId(openid);
                    user.setUserName(mapper.writeValueAsString(rootNode.path("nickName")).replaceAll("\"", ""));
                    user.setUserImg(mapper.writeValueAsString(rootNode.path("avatarUrl")).replaceAll("\"", ""));
                    // 插入数据库
                    userMapper.insert(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果用户已经存在，直接返回id
        return openid;
    }

    @Override
    public Integer createActivity(String activityName, String activityStart, String activityEnd, String sponsorId) throws ParseException {
        // 先把字符串转成String，然后组装成实体类传递给Mapper
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 插入总表
        Activity activity = new Activity();
        activity.setActivityName(activityName);
        activity.setActivityStart(sdf.parse(activityStart));
        activity.setActivityEnd(sdf.parse(activityEnd));
        activity.setSponsorId(sponsorId);
        activityMapper.insert(activity);
        // 返回活动ID
        return activity.getActivityId();
    }

    @Override
    public List<Map> viewJoinActivity(String userId) {
        List<UserActivity> userActivities = userActivityMapper.selectByUserId(userId);
        List<Integer> activityIds = new ArrayList<>();
        for (int i = 0; i < userActivities.size(); i++) {
            activityIds.add(userActivities.get(i).getActivityId());
        }
        List<Activity> activities = activityMapper.selectUserIdsByActivityIds(activityIds);
        List<String> userIds = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            userIds.add(activities.get(i).getSponsorId());
        }
        List<User> users = userMapper.selectUsersByPrimaryKey(userIds);

        List<Map> maps = new ArrayList<>();

        for (int i = 0; i < userActivities.size(); i++) {
            Map map = new HashMap();
            map.put("activityName", activities.get(i).getActivityName());
            map.put("sponsorName", users.get(i).getUserName());
            map.put("userActivity", userActivities.get(i));
            maps.add(map);
        }
        return maps;
    }

    @Override
    public List<Activity> viewSponsorActivity(String userId) {
        return activityMapper.selectByUserId(userId);
    }

    @Override
    public Object viewActivityDetail(Boolean isInvite, String openid, Boolean isSponsor, Integer Id) {
        Map map = new HashMap<>();
        // 如果是发起人就直接返回总表信息
        if (isSponsor) {
            // 得到所有已提交用户的头像url
            List<String> userIds = userActivityMapper.selectByActivityID(Id);
            if (userIds != null && userIds.size() != 0) {
                List<User> users = userMapper.selectUsersByPrimaryKey(userIds);
                List<String> imgs = new ArrayList<>();
                for (User user : users) {
                    imgs.add(user.getUserImg());
                }
                map.put("imgs", imgs);
            } else {
                map.put("imgs", null);
            }
            // 得到活动信息
            map.put("activity", activityMapper.selectByPrimaryKey(Id));
        } else {
            if (isInvite) {
                // 如果是从邀请链接进去，那么id肯定是总表id，所以去总表中查询对应活动信息
                Activity activity = activityMapper.selectByPrimaryKey(Id);
                String userName = userMapper.selectByPrimaryKey(activity.getSponsorId()).getUserName();
                map.put("sponsorName", userName);
                // 如果该活动已经完成，直接返回总表信息
                if (activity.getActivityState() >= 1) {
                    map.put("activity", activity);
                } else {
                    UserActivity userActivity = userActivityMapper.selectByUserIdAndActivityID(openid, Id);
                    if (userActivity == null) {            // 如果分表中没有记录，说明是第一次点击链接
                        userActivity = new UserActivity();
                        userActivity.setUserId(openid);
                        userActivity.setActivityId(Id);
                        userActivity.setIsJoin(false);
                        Byte b = 0;
                        userActivity.setState(b);
                        userActivityMapper.insertUserActivity(userActivity); // 向分表中插入一条数据
                    }
                    map.put("activityTime", activity.getActivityTime());
                    map.put("activityName", activity.getActivityName());
                    map.put("activityStart", activity.getActivityStart());
                    map.put("activityEnd", activity.getActivityEnd());
                    map.put("userActivity", userActivity);
                }
            } else {
                // 如果是正常查看我参与的活动，Id是分表的id
                UserActivity userActivity = userActivityMapper.selectByPrimaryKey(Id);
                Activity activity = activityMapper.selectByPrimaryKey(userActivity.getActivityId());
                String userName = userMapper.selectByPrimaryKey(activity.getSponsorId()).getUserName();
                map.put("sponsorName", userName);
                if (userActivity.getState() >= 2) {// 如果该活动已经完成，那么返回总表的信息
                    map.put("activity", activity);
                } else {// 如果该活动没有完成，返回分表信息
                    map.put("activityTime", activity.getActivityTime());
                    map.put("activityName", activity.getActivityName());
                    map.put("activityStart", activity.getActivityStart());
                    map.put("activityEnd", activity.getActivityEnd());
                    map.put("userActivity", userActivity);
                }
            }
        }
        return map;
    }

}
