package org.nocode.timing.mapper;

import org.nocode.timing.pojo.Activity;

import java.util.List;

/**
 * @Author HanZhao
 * @Description 对activity表的增删改查
 * @Date 2019/4/12
 */
public interface ActivityMapper {

    // 总表中插入一条活动记录，返回活动id
    int insert(Activity activity);

    // 通过用户id查找用户发起的活动
    List<Activity> selectByUserId(String userId);

    // 通过活动id查找活动
    Activity selectByPrimaryKey(Integer activityId);

}
