package com.wuyi.wcrawler.mapper;

import com.wuyi.wcrawler.entity.ZhUser;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @authort wuyi5
 * @date 2017/10/25.
 */
public interface ZhUserMapper extends Mapper<ZhUser> {
    void insertList(List<ZhUser> zhUserList);
    List<ZhUser> selectBySharding(Map<String, Object> paramMap);
    void updateUserStatus(List<Integer> ids);
}
