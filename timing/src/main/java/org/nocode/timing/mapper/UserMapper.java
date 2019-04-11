package org.nocode.timing.mapper;

import org.nocode.timing.pojo.User;

import java.util.List;

/**
 * @Author HanZhao
 * @Description
 * @Date 2019/4/10
 */
public interface UserMapper {

    // 根据用户名列查询用户列表，注意此处的名字，参数，返回值要和xml对应
    List<User> findUserByName(String userName) throws Exception;

}
