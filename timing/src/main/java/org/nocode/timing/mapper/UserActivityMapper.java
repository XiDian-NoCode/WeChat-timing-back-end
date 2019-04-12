package org.nocode.timing.mapper;

import org.apache.ibatis.annotations.Param;
import org.nocode.timing.pojo.UserActivity;

import java.util.List;

/**
 * @Author HanZhao
 * @Description 对user_activity表的增删改查
 * @Date 2019/4/12
 */
public interface UserActivityMapper {

    // 通过用户id查找用户参与的活动
    List<UserActivity> selectByUserId(String userId);
}
