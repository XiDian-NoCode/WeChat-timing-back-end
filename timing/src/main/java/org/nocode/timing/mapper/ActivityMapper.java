package org.nocode.timing.mapper;

import org.nocode.timing.pojo.Activity;

public interface ActivityMapper {
    int updateActivityTimeByActivityId(Activity activity);
}
