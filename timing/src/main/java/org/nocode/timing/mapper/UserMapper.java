package org.nocode.timing.mapper;

import org.apache.ibatis.annotations.Param;
import org.nocode.timing.pojo.User;

import java.util.List;

/**
 * @Author HanZhao and PanLei
 * @Description 对user表的增删改查
 * @Date 2019/4/18
 */
public interface UserMapper {

    // 插入新用户
    int insert(User user);

    // 查找用户
    User selectByPrimaryKey(String userId);

    // 查找多个用户
    List<User> selectUsersByPrimaryKey(@Param("userIds") List<String> userIds);

    // 根据用户id查询user
    User queryUserById(String userId);

}
