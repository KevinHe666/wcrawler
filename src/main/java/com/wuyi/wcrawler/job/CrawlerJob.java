package com.wuyi.wcrawler.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.service.impl.CrawlerUrlServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author wuyi5
 * @date 2017/8/16
 */
public class CrawlerJob implements SimpleJob {
    private static final Log LOG = LogFactory.getLog(CrawlerJob.class);
    
    @Autowired
    private CrawlerUrlServiceImpl crawlerUrlService;
    @Override
    public void execute(ShardingContext shardingContext) {
        LOG.info("CrawlerJob started.");
        int shardingItem = shardingContext.getShardingItem();
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        LOG.info("ShardingItem(" + shardingItem + ")");
        List<ZhUser> users = crawlerUrlService.fetchUrl(shardingItem, shardingTotalCount);
        if (users.size() == 0 || users == null) {
            LOG.error("ZhUser list is empty or null.");
            System.exit(-1);
        }
        LOG.info("ZhUser list's  size() is " + users.size());
        crawlerUrlService.crawler(users);
    }
}
