package org.nocode.timing.mapper;

import org.nocode.timing.pojo.Activity;

import java.util.List;

public interface ActivityMapper {
    // 更新
    int updateActivityTimeByActivityId(Activity activity);
    // 查询
    List<Activity> queryActivity(Activity activity);
}
