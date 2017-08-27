package com.wuyi.wcrawler.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.wuyi.wcrawler.bean.CrawlerUrlBean;
import com.wuyi.wcrawler.dao.CrawlerUrlDao;
import com.wuyi.wcrawler.service.impl.CrawlerUrlServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wuyi5 on 2017/8/16.
 */
public class CrawlerUrlJob implements SimpleJob {
    private static final Log LOG = LogFactory.getLog(CrawlerUrlJob.class);
    
    @Autowired
    private CrawlerUrlServiceImpl crawlerUrlService;
    public void execute(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        LOG.info("ShardingItem(" + shardingItem + ")");
        List<CrawlerUrlBean> urls = crawlerUrlService.fetchUrl(shardingItem, shardingTotalCount);
        System.out.println(urls);
//        crawlerUrlService.crawler(urls);
    }
}
