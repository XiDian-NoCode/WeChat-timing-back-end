package org.nocode.timing.service;

import org.nocode.timing.pojo.Activity;
import org.nocode.timing.pojo.UserActivity;

import java.util.List;

/**
 * @Author HanZhao
 * @Description 所有使用者的公有功能
 * @Date 2019/4/18
 */
public interface UserService {

    // 登陆
    String login(String code, String encryptedData, String iv) throws Exception;

    // 创建活动
    Integer createActivity(String activityName, String activityStart, String activityEnd, String useId) throws Exception;

    // 查看我参与的活动
    List<UserActivity> viewJoinActivity(String userId);

    // 查看我发起的活动
    List<Activity> viewSponsorActivity(String userId);

    // 查看某个活动细节
    Activity viewActivityDetail(Integer activityId);

    // 点击邀请链接
    Object clickInviteLink(String code, String encryptedData, String iv, Integer activityId) throws Exception;

}
