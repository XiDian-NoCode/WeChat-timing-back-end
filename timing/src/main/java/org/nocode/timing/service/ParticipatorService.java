package org.nocode.timing.service;

/**
 * @Author PanLei
 * @Description 参与者独有的功能
 * @Date 2019/4/18
 */
public interface ParticipatorService {

    int commitMyTime(int userActivityId, String userBusyTime, String openid, String formid);

}
