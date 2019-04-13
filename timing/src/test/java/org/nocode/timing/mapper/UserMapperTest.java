package org.nocode.timing.mapper;

import org.junit.Test;
import org.nocode.timing.BaseTest;
import org.nocode.timing.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testQueryUserById() {
        User user = userMapper.queryUserById("111");
        System.out.println(user.getUserId());
        System.out.println(user.getUserImg());
        System.out.println(user.getUserName());
    }
}
