package com.wuyi.wcrawler.mapper;

import com.wuyi.wcrawler.entity.ZhUser;
import tk.mybatis.mapper.common.Mapper;
import com.wuyi.wcrawler.entity.FollowRelation;

import java.util.List;

/**
 * @authort wuyi
 * @date 2017/11/9.
 */
public interface FollowRelationMapper extends Mapper<FollowRelation>{
    void insertList(List<FollowRelation> followRelationList);
}
