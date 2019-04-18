package org.nocode.timing.mapper;

import org.apache.ibatis.annotations.Param;
import org.nocode.timing.pojo.UserActivity;

import java.util.List;

/**
 * @Author HanZhao and PanLei
 * @Description 对user_activity表的增删改查
 * @Date 2019/4/18
 */
public interface UserActivityMapper {

    // 通过用户id查找用户参与的活动
    List<UserActivity> selectByUserId(String userId);

    // 通过用户id和活动id查找用户参与的活动
    UserActivity selectByUserIdAndActivityID(@Param("userId") String userId, @Param("activityId") Integer activityId);

    // 添加用户活动：分表
    int insertUserActivity(UserActivity userActivity);

    // 根据条件,查询userActivity
    List<UserActivity> queryUserActivity(UserActivity userActivity);

    // 更新UserActivity:这个用于 总表确认时间 时分表的更新,需要根据 `activity_id` + `is_join`=1 更新其 `state`=2或者3
    int updateUserActivity(UserActivity userActivity);

    // 更新UserActivity:这个用于 分表确认时间 时分表的更新,需要根据`userActivityId` 更新 分表 中的 userBusyTime, isJoin = 1, state = 1
    int updateUserActivityForCommitMyTime(UserActivity userActivity);

}
