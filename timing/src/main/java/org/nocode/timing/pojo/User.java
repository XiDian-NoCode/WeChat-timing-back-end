package org.nocode.timing.pojo;

/**
 * @Author HanZhao and PanLei
 * @Description 对应user表的实体类
 * @Date 2019/4/18
 */
public class User {

    private String userId;

    private String userName;

    private String userImg;

    private String userFormId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg == null ? null : userImg.trim();
    }

    public String getUserFormId() {
        return userFormId;
    }

    public void setUserFormId(String userFormId) {
        this.userFormId = userFormId;
    }

}
