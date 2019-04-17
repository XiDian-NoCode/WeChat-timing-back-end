package org.nocode.timing.mapper;

import org.nocode.timing.pojo.User;

public interface UserMapper {
    /**
     * 根据用户id查询user
     * @return
     */
    User queryUserById(String userId);

	//Author HanZhao
    // 插入新用户
    int insert(User user);

    // 查找用户
    User selectByPrimaryKey(String userId);

}
