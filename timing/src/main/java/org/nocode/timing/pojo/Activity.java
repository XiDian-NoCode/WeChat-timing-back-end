package org.nocode.timing.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author HanZhao and PanLei
 * @Description 对应activity表的实体类
 * @Date 2019/4/18
 */
public class Activity {

    private Integer activityId;

    private String activityName;

    private Date activityStart;

    private Date activityEnd;

    private String activityLocation;

    private String activityTime;

    private Byte activityState;

    private Integer activityMembers;

    private String totalBusyTime;

    private Byte schedule;

    private String sponsorId;

    private String sponsorName;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    // 时间类型需要转换
    public String getActivityStart() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(activityStart);
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    // 时间类型需要转换
    public String getActivityEnd() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(activityEnd);
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime == null ? null : activityTime.trim();
    }

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public Byte getActivityState() {
        return activityState;
    }

    public void setActivityState(Byte activityState) {
        this.activityState = activityState;
    }

    public Integer getActivityMembers() {
        return activityMembers;
    }

    public void setActivityMembers(Integer activityMembers) {
        this.activityMembers = activityMembers;
    }

    public String getTotalBusyTime() {
        return totalBusyTime;
    }

    public void setTotalBusyTime(String totalBusyTime) {
        this.totalBusyTime = totalBusyTime == null ? null : totalBusyTime.trim();
    }

    public Byte getSchedule() {
        return schedule;
    }

    public void setSchedule(Byte schedule) {
        this.schedule = schedule;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId == null ? null : sponsorId.trim();
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

}
