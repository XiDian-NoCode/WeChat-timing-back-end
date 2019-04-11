package org.nocode.timing.pojo;

import java.util.Date;

/**
 * @Author HanZhao
 * @Description
 * @Date 2019/4/11
 */
public class Activity {
    private Integer activityId;

    private String activityName;

    private Date activityStart;

    private Date activityEnd;

    private String activityTime;

    private Byte activityState;

    private Integer activityMembers;

    private String totalBusyTime;

    private Byte schedule;

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

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
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
}