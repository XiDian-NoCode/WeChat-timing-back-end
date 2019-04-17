package org.nocode.timing.mapper;

import org.nocode.timing.pojo.Activity;

import java.util.List;


public interface ActivityMapper {
	// 潘磊
    // 更新
    int updateActivityTimeByActivityId(Activity activity);
    // 查询
    List<Activity> queryActivity(Activity activity);

	// 韩钊
    // 总表中插入一条活动记录，返回活动id
    int insert(Activity activity);

    // 通过用户id查找用户发起的活动
    List<Activity> selectByUserId(String userId);

    // 通过活动id查找活动
    Activity selectByPrimaryKey(Integer activityId);

}
