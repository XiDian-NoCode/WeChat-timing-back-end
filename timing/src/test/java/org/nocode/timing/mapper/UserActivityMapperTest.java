package org.nocode.timing.mapper;

import org.junit.Ignore;
import org.junit.Test;
import org.nocode.timing.BaseTest;
import org.nocode.timing.pojo.UserActivity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserActivityMapperTest extends BaseTest {
    @Autowired
    private UserActivityMapper userActivityMapper;

    @Test
    @Ignore
    public void testInsertUserActivity() {
        UserActivity userActivity = new UserActivity();
        userActivity.setActivityId(1);
        userActivity.setUserId("111");
        userActivity.setIsJoin(false);
        byte state = 0;
        userActivity.setState(state);
        int i = userActivityMapper.insertUserActivity(userActivity);
        assertEquals(1,i);
    }

    @Test
    public void testQueryUserActivity() {
        UserActivity userActivity = new UserActivity();
        userActivity.setActivityId(24);
        byte state = 2;
        userActivity.setState(state);
        userActivity.setIsJoin(true);
        List<UserActivity> userActivities = userActivityMapper.queryUserActivity(userActivity);
//        assertEquals(5,userActivities.size());
        for(UserActivity u:userActivities) {
            System.out.println(u.getUserBusyTime());
        }
    }
}
