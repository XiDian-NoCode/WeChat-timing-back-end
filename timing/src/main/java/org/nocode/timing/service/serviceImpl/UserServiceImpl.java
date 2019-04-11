package org.nocode.timing.service.serviceImpl;

import org.nocode.timing.mapper.UserMapper;
import org.nocode.timing.pojo.User;
import org.nocode.timing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author HanZhao
 * @Description
 * @Date 2019/4/10
 */
@Service("userServiceImpl")// 默认首字母小写
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    // 采用set方法自动注入
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 根据用户名列查询用户列表
    @Override
    public List<User> findUserByName(String userName) throws Exception {
        return userMapper.findUserByName(userName);
    }

}
