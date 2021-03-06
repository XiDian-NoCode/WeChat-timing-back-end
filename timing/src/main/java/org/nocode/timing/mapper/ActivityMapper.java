package org.nocode.timing.mapper;

import org.apache.ibatis.annotations.Param;
import org.nocode.timing.pojo.Activity;

import java.util.List;

/**
 * @Author HanZhao and PanLei
 * @Description 对activity表的增删改查
 * @Date 2019/4/18
 */
public interface ActivityMapper {

    // 总表中插入一条活动记录，返回活动id
    int insert(Activity activity);

    // 通过用户id查找用户发起的活动
    List<Activity> selectByUserId(String userId);

    // 通过活动id查找所有的活动
    List<Activity> selectUserIdsByActivityIds(@Param("activityIds") List<Integer> activityIds);

    // 通过活动id查找活动
    Activity selectByPrimaryKey(Integer activityId);

    // 根据活动id更新
    int updateActivityTimeByActivityId(Activity activity);

    // 查询
    List<Activity> queryActivity(Activity activity);

}
