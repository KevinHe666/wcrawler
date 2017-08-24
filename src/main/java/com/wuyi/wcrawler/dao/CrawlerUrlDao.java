package com.wuyi.wcrawler.dao;

import com.wuyi.wcrawler.bean.CrawlerUrlBean;

import java.util.List;
import java.util.Map;

/**
 * Created by wuyi5 on 2017/8/16.
 */
public interface CrawlerUrlDao {
    List<CrawlerUrlBean> select(Map<String, Object> url_param_map);
    void updateUrlStatus(List<Integer> ids);
}
