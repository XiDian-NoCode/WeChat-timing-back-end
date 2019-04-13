package org.nocode.timing.mapper;

import org.nocode.timing.pojo.User;

public interface UserMapper {
    /**
     * 根据用户id查询user
     * @return
     */
    User queryUserById(String userId);
}
