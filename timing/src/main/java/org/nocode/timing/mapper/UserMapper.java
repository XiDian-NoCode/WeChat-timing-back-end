package org.nocode.timing.mapper;

import org.nocode.timing.pojo.User;

/**
 * @Author HanZhao
 * @Description 对user表的增删改查
 * @Date 2019/4/12
 */
public interface UserMapper {

    // 插入新用户
    int insert(User user);

    // 查找用户
    User selectByPrimaryKey(String userId);
}
