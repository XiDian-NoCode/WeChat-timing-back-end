package org.nocode.timing.pojo;

/**
 * @Author HanZhao
 * @Description 对应user_activity表
 * @Date 2019/4/12
 */
public class UserActivity {
    private Integer userActivityId;

    private Integer activityId;

    private String userId;

    private String userBusyTime;

    private Boolean isJoin;

    private Byte state;

    public Integer getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Integer userActivityId) {
        this.userActivityId = userActivityId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserBusyTime() {
        return userBusyTime;
    }

    public void setUserBusyTime(String userBusyTime) {
        this.userBusyTime = userBusyTime == null ? null : userBusyTime.trim();
    }

    public Boolean getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(Boolean isJoin) {
        this.isJoin = isJoin;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}
