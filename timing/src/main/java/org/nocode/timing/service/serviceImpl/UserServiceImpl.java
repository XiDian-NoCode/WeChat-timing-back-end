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

    /**
     * @param code
     * @param encryptedData
     * @param iv
     * @return openid
     * 此处需要调用微信服务器
     * 首先使用code可以得到openid和session_key
     * 然后先用openid查询数据库中是否已存在该用户,存在的话检查其昵称和头像有没有改变
     * 如果不存在，则还需使用session_key、encryptedData、iv来解密用户信息，得到用户昵称和头像，目前暂时需要这两个信息
     */
    @Override
    public String login(String code, String encryptedData, String iv) throws IOException {

        // 调用访问微信服务器工具方法，传入三个参数获取带有openid、session_key的json字符串
        String jsonId = GetOpenIdUtil.getOpenId(code);
        System.out.println(jsonId);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonId);
        // 从json字符串获取openid和session_key
        String openid = mapper.writeValueAsString(rootNode.path("openid")).replaceAll("\"", "");
        String session_key = mapper.writeValueAsString(rootNode.path("session_key"));
        // 得到用户信息
        String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
        rootNode = mapper.readTree(result);
        String nickName = mapper.writeValueAsString(rootNode.path("nickName")).replaceAll("\"", "");
        String avatarUrl = mapper.writeValueAsString(rootNode.path("avatarUrl")).replaceAll("\"", "");
        // 数据库查询用户是否已经存在
        User user = userMapper.selectByPrimaryKey(openid);
        // 如果用户不存在，用户信息存入数据库
        if (user == null) {
            user = new User();
            user.setUserId(openid);
            user.setUserName(nickName);
            user.setUserImg(avatarUrl);
            // 插入数据库
            userMapper.insert(user);
        } else if (!nickName.equals(user.getUserName()) || !avatarUrl.equals(user.getUserImg())) {// 如果昵称改变或者头像改变
            userMapper.updateInfoById(openid, nickName, avatarUrl);
        }
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
        activity.setSponsorName(userMapper.selectByPrimaryKey(sponsorId).getUserName());
        activityMapper.insert(activity);
        // 返回活动ID
        return activity.getActivityId();
    }

    /**
     * @param userId
     * @return 活动列表
     * 由于分表中没有活动名，发起人昵称，因此，查看我参与的活动时需要先查询分表，然后用分表中的活动id在总表中查询活动名和发起人昵称
     */
    @Override
    public List<Map> viewJoinActivity(String userId) {
        List<Map> maps = new ArrayList<>();
        List<UserActivity> userActivities = userActivityMapper.selectByUserId(userId);
        // 没有我参与的活动
        if (userActivities == null || userActivities.size() == 0) {
            return maps;
        }
        List<Integer> activityIds = new ArrayList<>();
        for (int i = 0; i < userActivities.size(); i++) {
            activityIds.add(userActivities.get(i).getActivityId());
        }
        List<Activity> activities = activityMapper.selectUserIdsByActivityIds(activityIds);

        for (int i = 0; i < userActivities.size(); i++) {
            Map map = new HashMap();
            map.put("activityName", activities.get(i).getActivityName());
            map.put("sponsorName", activities.get(i).getSponsorName());
            map.put("userActivity", userActivities.get(i));
            maps.add(map);
        }
        return maps;
    }

    @Override
    public List<Activity> viewSponsorActivity(String userId) {
        return activityMapper.selectByUserId(userId);
    }

    /**
     * @param openid
     * @param Id
     * @param isSponsor
     * @param isInvite
     * @return 发起人和参与者返回的不全一样
     * 情况1：发起人，直接返回总表的信息，还需返回所有参与者的头像，因为显示数量限制，只返回4个头像
     * 情况2：参与者：
     * * * 情况1：从邀请链接第一次点击进入，需要在分表中先插入一条数据，然后返回分表的信息+总表的一部分信息
     * * * 情况2：从查看我的活动点击进入，说明不是第一次进入，直接返回分表的信息+总表的一部分信息
     */
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
                // 显示四个头像
                for (int i = 0; i < users.size(); i++) {
                    if (i < 4) {
                        imgs.add(users.get(i).getUserImg());
                    } else {
                        break;
                    }
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
                    putActivityInfo(activity, map);
                    map.put("userActivity", userActivity);
                }
            } else {
                // 如果是正常查看我参与的活动，Id是分表的id
                UserActivity userActivity = userActivityMapper.selectByPrimaryKey(Id);
                Activity activity = activityMapper.selectByPrimaryKey(userActivity.getActivityId());
                if (userActivity.getState() >= 2) {// 如果该活动已经完成，那么返回总表的信息
                    map.put("activity", activity);
                } else {// 如果该活动没有完成，返回分表信息
                    putActivityInfo(activity, map);
                    map.put("userActivity", userActivity);
                }
            }
        }
        return map;
    }

    private void putActivityInfo(Activity activity, Map map) {
        map.put("sponsorName", activity.getSponsorName());
        map.put("state", activity.getActivityState());
        map.put("activityTime", activity.getActivityTime());
        map.put("activityLocation", activity.getActivityLocation());
        map.put("activityName", activity.getActivityName());
        map.put("activityStart", activity.getActivityStart());
        map.put("activityEnd", activity.getActivityEnd());
    }

}
