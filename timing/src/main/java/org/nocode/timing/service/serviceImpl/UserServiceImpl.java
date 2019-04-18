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
import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    public static final String APPID = "wx0885230cd5c5837d";
    public static final String SECRET = "5dabd06fceb7c6cd07bd437fa8c07269";

    private User user;
    private UserMapper userMapper;
    private ActivityMapper activityMapper;
    private Activity activity;
    private UserActivityMapper userActivityMapper;

    @Autowired
    public void setUser(User user) {
        this.user = user;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setActivityMapper(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    @Autowired
    public void setActivity(Activity activity) {
        this.activity = activity;
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
        activity.setActivityName(activityName);
        activity.setActivityStart(sdf.parse(activityStart));
        activity.setActivityEnd(sdf.parse(activityEnd));
        activity.setSponsorId(sponsorId);
        activityMapper.insert(activity);
        // 返回活动ID
        return activity.getActivityId();
    }

    @Override
    public List<UserActivity> viewJoinActivity(String userId) {
        return userActivityMapper.selectByUserId(userId);
    }

    @Override
    public List<Activity> viewSponsorActivity(String userId) {
        return activityMapper.selectByUserId(userId);
    }

    @Override
    public Activity viewActivityDetail(Integer activityId) {
        return activityMapper.selectByPrimaryKey(activityId);
    }

    @Override
    public Object clickInviteLink(String code, String encryptedData, String iv, Integer activityId) throws IOException {
        // 首先调用登陆服务，得到用户id
        String openid = login(code, encryptedData, iv);
        // 去总表中查询对应活动信息
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        UserActivity userActivity = userActivityMapper.selectByUserIdAndActivityID(openid, activityId);
        // 如果不是发起人点击，则分表会有记录
        if (userActivity != null) {
            // 构造一个局部内部类
            class Inner {
                Activity activity;
                UserActivity userActivity;

                public Inner(Activity activity, UserActivity userActivity) {
                    this.activity = activity;
                    this.userActivity = userActivity;
                }

                public Activity getActivity() {
                    return activity;
                }

                public void setActivity(Activity activity) {
                    this.activity = activity;
                }

                public UserActivity getUserActivity() {
                    return userActivity;
                }

                public void setUserActivity(UserActivity userActivity) {
                    this.userActivity = userActivity;
                }
            }
            return new Inner(activity, userActivity);
        } else {
            return activity;
        }
    }

}
