package org.nocode.timing.controller;

import org.nocode.timing.pojo.Activity;
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
 * @Date 2019/4/18
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
        String activityName = (String) map.get("activityName");
        String activityStart = map.get("activityStart").toString();
        String activityEnd = map.get("activityEnd").toString();
        String sponsorId = map.get("sponsorId").toString();
        map = new HashMap();
        int state = -1;
        if (activityName == null || activityName.length() == 0) {
            map.put("activityId", state);
            return map;
        }
        // 告诉前端创建成功
        state = userServiceImpl.createActivity(activityName, activityStart, activityEnd, sponsorId);
        // 用map存自动转换成json格式
        map.put("activityId", state);
        return map;
    }

    // 查看我参与的活动
    @RequestMapping(value = "/viewJoinActivity")
    @ResponseBody
    public List<Map> viewJoinActivity(@RequestBody Map map) {
        return userServiceImpl.viewJoinActivity(map.get("userId").toString());
    }

    // 查看我发起的活动
    @RequestMapping(value = "/viewSponsorActivity")
    @ResponseBody
    public List<Activity> viewSponsorActivity(@RequestBody Map map) {
        return userServiceImpl.viewSponsorActivity(map.get("userId").toString());
    }

    // 查看某个活动细节
    @RequestMapping(value = "/viewActivityDetail")
    @ResponseBody
    public Object viewActivityDetail(@RequestBody Map map) {
        return userServiceImpl.viewActivityDetail((Boolean) map.get("isInvite"), (String) map.get("openid"), (Boolean) map.get("isSponsor"), Integer.parseInt(map.get("Id").toString()));
    }

}
