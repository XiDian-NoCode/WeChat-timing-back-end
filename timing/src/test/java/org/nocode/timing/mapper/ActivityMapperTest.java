package org.nocode.timing.mapper;

import org.junit.Test;
import org.nocode.timing.BaseTest;
import org.nocode.timing.pojo.Activity;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ActivityMapperTest extends BaseTest {
    @Autowired
    private ActivityMapper activityMapper;

    @Test
    public void testUpdateActivityTimeByActivityId() {
        Activity activity = new Activity();
        activity.setActivityId(1);
        activity.setActivityTime("1995");
        int i = activityMapper.updateActivityTimeByActivityId(activity);
        assertEquals(1,i);
    }
}
