package org.nocode.timing.controller;

import org.nocode.timing.pojo.User;
import org.nocode.timing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author HanZhao
 * @Description
 * @Date 2019/4/10
 */
@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/findUserByName", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
        // @ResponseBody可以返回json数据
    List<User> findUserByName(@RequestParam("userName") String userName) throws Exception { // @RequestParam对应前端的post请求的参数名
        return userService.findUserByName(userName);
    }

}
