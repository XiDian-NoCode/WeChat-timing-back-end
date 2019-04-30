package org.nocode.timing.service;

import org.nocode.timing.pojo.Activity;

import java.util.List;
import java.util.Map;

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

    // 查看邀请我的活动
    List<Map> viewJoinActivity(String userId);

    // 查看我发起的活动
    List<Activity> viewSponsorActivity(String userId);

    // 查看某个活动细节，这里需要考虑多种情况，1.从邀请链接点击进入，2.从我的活动点击进入
    Object viewActivityDetail(Boolean isInvite, String openid, Boolean isSponsor, Integer Id);

}
