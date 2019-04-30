package org.nocode.timing.controller;

import org.nocode.timing.service.ParticipatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author PanLei
 * @Description
 * @Date 2019/4/18
 */
@Controller
@RequestMapping("/participator")
public class ParticipatorController {

    @Autowired
    private ParticipatorService participateService;

    /**
     * @return 功能: 参与人 提交自己的时间安排
     * 流程:
     * service层
     * 根据 `userActivityId` 更新 分表 中的 userBusyTime, isJoin = 1, state = 1
     * 然后获取这条数据，由此获取到其 对应的 总表id
     * 然后根据 这条数据的 activityId 来更新 总表 中的 activityMember+1, totalBusyTime 相应位置加 1
     * controller层
     * 调用 service 层方法, 获取更新结果,将处理结果返回给前端
     */
    @RequestMapping(value = "/commitmytime")
    @ResponseBody
    public Map commitMyTime(@RequestBody Map map) {
        Map<String, String> resultMap = new HashMap<>();
        int userActivityId = (int) map.get("userActivityId");
        // 格式是01010……
        String userBusyTime = (String) map.get("userBusyTime");
        // result = 0(失败),1(成功)

        // formid存入数据库，时效7天，只能用一次
        String openid = (String) map.get("openid");
        String formid = (String) map.get("formid");
        int result = participateService.commitMyTime(userActivityId, userBusyTime, openid, formid);

        if (result <= 0) {
            resultMap.put("success", "error");
            return resultMap;
        } else {
            resultMap.put("success", "success");
            return resultMap;
        }
    }

}
