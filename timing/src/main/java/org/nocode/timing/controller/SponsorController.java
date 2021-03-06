package org.nocode.timing.controller;

import org.nocode.timing.pojo.User;
import org.nocode.timing.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author PanLei
 * @Description 发起人相关操作
 * @Date 2019/4/18
 */
@Controller
@RequestMapping("/sponsor")
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;

    /**
     * @param map : 接收json格式参数
     *            activityId:总表活动id
     *            sponsorId:用户id
     *            index:单元格索引
     *            num :人数
     * @return 功能: 发起人 点击 我发起的活动 中的 某一个已经确定活动时间 的活动, 点击某一个显示有几个人有事的单元格 查看有事的人哪几位
     * 流程:
     * service层
     * 根据 `activityId` + `is_join`=1 + `state`=2 查询到 分表 中的 数据列表
     * 对活动列表遍历,获取`user_busy_time`,根据index定位到具体的,看它是否为 '1',是的话就保存到 user列表
     * controller层
     * 调用 service 层方法,获取到最终的  user列表,返回给 前端
     */
    @RequestMapping("/checkmember")
    @ResponseBody
    public Map checkMember(@RequestBody Map map) {
        int activityId = Integer.parseInt((String) map.get("activityId"));
        String sponsorId = (String) map.get("sponsorId");
        int index = Integer.parseInt((String) map.get("index"));
        int num = Integer.parseInt((String) map.get("num"));

        List<User> userList = sponsorService.checkMember(activityId, sponsorId, index, num);
        Map<String, List<User>> userMap = new HashMap<>();
        userMap.put("users", userList);
        return userMap;
    }

    // 发起人确定活动时间，需要将总表状态改为已确定时间（1），对应的所有分表中 已提交未确定时间状态（1）改为已提交确定时间状态（2）
    // 未提交状态(0) 改为 已结束(1)
    // 根据所选单元格,然后查看开始时间 ,计算出活动时间

    /**
     * @param map : 接收json格式参数
     *            String activityTime : 所选活动时间的单元格,可以多个
     *            int activityId:总表活动id
     * @return 流程:
     * service层:
     * 根据 `activityId` 在总表中 更新 `activity_time` 和 `activity_state`=1
     * 然后在 分表 中,根据 `activity_id` + `state`=1 + `is_join`=1 更新其 `state`=2
     * 根据 `activity_id` + `state`=0 + `is_join`=0 更新其 `state`=3
     * 通知所有已经参与的成员
     * controller层:
     * 调用 service 层方法,返回结果是否成功，返回通知成功的成员数量
     */
    @RequestMapping("/commitfinaltime")
    @ResponseBody
    public Map commitFinalTime(@RequestBody Map map) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        int activityId = Integer.parseInt((String) map.get("activityId"));
        String activityTime = (String) map.get("activityTime");
        String result = sponsorService.commitFinalTime(activityId, activityTime);
        resultMap.put("success", result);
        // 如果提交成功，通知所有成员
        if (resultMap.get("success").equals("success")) {
            String count = sponsorService.informMember(activityId);
            if (count == null) {
                resultMap.put("count", "0");
            } else {
                resultMap.put("count", count);
            }
        } else {
            resultMap.put("count", "0");
        }
        return resultMap;
    }

}
