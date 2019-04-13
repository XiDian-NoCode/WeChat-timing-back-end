package org.nocode.timing.service;

import org.nocode.timing.pojo.User;

import java.util.List;

public interface SponsorService {
    List<User> checkMember(int activityId, String userId, int index, int num);
    String commitFinalTime(int activityId, String activityTime);
}
