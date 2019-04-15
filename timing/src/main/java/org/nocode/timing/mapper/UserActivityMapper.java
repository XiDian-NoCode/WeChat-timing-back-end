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

    /**
     * 更新UserActivity:这个用于 总表确认时间 时分表的更新
     * 需要根据 `activity_id` + `is_join`=1 更新其 `state`=2或者3
     * @return
     */
    int updateUserActivity(UserActivity userActivity);

    /**
     * 更新UserActivity:这个用于 分表确认时间 时分表的更新
     * 需要根据`userActivityId` 更新 分表 中的 userBusyTime, isJoin = 1, state = 1
     * @return
     */
    int updateUserActivityForCommitMyTime(UserActivity userActivity);
}
