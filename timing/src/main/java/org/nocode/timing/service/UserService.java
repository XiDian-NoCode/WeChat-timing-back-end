package org.nocode.timing.service;

import org.nocode.timing.pojo.Activity;

import java.util.List;

/**
 * @Author HanZhao
 * @Description 所有使用者的公有功能
 * @Date 2019/4/12
 */
public interface UserService {

    // 登陆
    String login(String code, String encryptedData, String iv) throws Exception;

    // 创建活动
    Integer createActivity(String activityName, String activityStart, String activityEnd, String useId) throws Exception;

    // 查看我参与的活动
    List<Activity> viewJoinActivity(String userId);

    // 查看我发起的活动
    List<Activity> viewSponsorActivity(String userId);

    // 查看某个活动细节
    Activity viewActivityDetail(String userId, String activityId);

}
