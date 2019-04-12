package org.nocode.timing.controller;

import org.nocode.timing.pojo.Activity;
import org.nocode.timing.pojo.UserActivity;
import org.nocode.timing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author HanZhao
 * @Description
 * @Date 2019/4/12
 */
@Controller
public class UserController {

    private UserService userServiceImpl;

    @Autowired
    public void setUserService(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    // 登陆，返回用户的openid
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map login(@RequestBody Map map) throws Exception {
        String code = (String) map.get("code");
        String encryptedData = (String) map.get("encryptedData");
        String iv = (String) map.get("iv");
        map = new HashMap();

        // 登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code不能为空");
            return map;
        }

        // 获取
        String openid = userServiceImpl.login(code, encryptedData, iv);

        map.put("status", 1);
        map.put("openId", openid);
        return map;
    }

    // 创建活动，返回活动ID
    @RequestMapping(value = "/createActivity")
    @ResponseBody
    public Map createActivity(@RequestBody Map map) throws Exception {
        // 生成一个状态码，告诉前端提交成功
        int state = userServiceImpl.createActivity((String) map.get("activityName"), (String) map.get("activityStart"), (String) map.get("activityEnd"), (String) map.get("sponsorId"));
        // 用map存自动转换成json格式
        map = new HashMap();
        map.put("activityId", state);
        return map;
    }

    @RequestMapping(value = "/viewJoinActivity")
    @ResponseBody
    public List<UserActivity> viewJoinActivity(@RequestBody Map map) {
        return userServiceImpl.viewJoinActivity((String) map.get("userId"));
    }

    @RequestMapping(value = "/viewSponsorActivity")
    @ResponseBody
    public List<Activity> viewSponsorActivity(@RequestBody Map map) {
        return userServiceImpl.viewSponsorActivity((String) map.get("userId"));
    }

    @RequestMapping(value = "/viewActivityDetail")
    @ResponseBody
    public Activity viewActivityDetail(@RequestBody Map map) {
        return userServiceImpl.viewActivityDetail(Integer.parseInt(map.get("activityId").toString()));
    }
}
