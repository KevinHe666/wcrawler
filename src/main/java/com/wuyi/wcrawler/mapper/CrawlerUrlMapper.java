package com.wuyi.wcrawler.mapper;

import com.wuyi.wcrawler.entity.CrawlerUrl;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @author wuyi5
 * @date 2017/8/16
 */
public interface CrawlerUrlMapper extends Mapper<CrawlerUrl>{
    List<CrawlerUrl> selectBySharding(Map<String, Object> url_param_map);
    void updateUrlStatus(List<Integer> ids);
}
