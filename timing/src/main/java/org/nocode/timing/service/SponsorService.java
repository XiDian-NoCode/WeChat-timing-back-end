package org.nocode.timing.service;

import org.nocode.timing.pojo.User;

import java.util.List;

/**
 * @Author PanLei
 * @Description 发起人独有的功能
 * @Date 2019/4/18
 */
public interface SponsorService {

    List<User> checkMember(int activityId, String userId, int index, int num);

    String commitFinalTime(int activityId, String activityTime);

    List<User> informMember(int activityId);

}
