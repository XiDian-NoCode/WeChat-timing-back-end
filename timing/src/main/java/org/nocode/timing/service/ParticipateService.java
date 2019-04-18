package org.nocode.timing.service;

/**
 * @Author PanLei
 * @Description 参与者独有的功能
 * @Date 2019/4/18
 */
public interface ParticipateService {
    int commitMyTime(int userActivityId, String userBusyTime);
}
