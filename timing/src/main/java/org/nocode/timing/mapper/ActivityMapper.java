package org.nocode.timing.mapper;

import org.nocode.timing.pojo.Activity;

/**
 * @Author HanZhao
 * @Description 对activity表的增删改查
 * @Date 2019/4/12
 */
public interface ActivityMapper {

    // 总表中插入一条活动记录，返回活动ID
    int insert(Activity activity);

}
