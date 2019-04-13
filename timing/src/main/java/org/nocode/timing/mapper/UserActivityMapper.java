package org.nocode.timing.mapper;

import org.nocode.timing.pojo.UserActivity;

import java.util.List;

public interface UserActivityMapper {
    /**
     * 添加用户活动：分表
     * @param userActivity
     * @return
     */
    int insertUserActivity(UserActivity userActivity);

    /**
     * 根据条件,查询userActivity
     * @param userActivity
     * @return
     */
    List<UserActivity> queryUserActivity(UserActivity userActivity);
}
