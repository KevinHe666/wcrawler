package com.wuyi.wcrawler.service;

import com.wuyi.wcrawler.entity.CrawlerUrl;

import java.util.List;

/**
 * Created by wuyi5 on 2017/8/17.
 */
public interface CrawlerUrlService {
    List<CrawlerUrl> fetchUrl(int shardingItem, int ShardingTotalCount);
    void crawler(List<CrawlerUrl> urls);
}
