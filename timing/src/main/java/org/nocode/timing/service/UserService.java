package org.nocode.timing.service;

import org.nocode.timing.pojo.User;

import java.util.List;

public interface UserService {

    // 根据用户名列查询用户列表
    List<User> findUserByName(String name) throws Exception;

}
