package com.wuyi.wcrawler.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.wuyi.wcrawler.service.ProxyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wuyi5 on 2017/8/28.
 */
public class ProxyJob implements SimpleJob {
    private Log LOG = LogFactory.getLog(ProxyJob.class);

    @Autowired
    private ProxyService proxyService;
    public void execute(ShardingContext shardingContext) {
        LOG.info("ProxyJob started.");
        int item = shardingContext.getShardingItem();
        String siteParser = shardingContext.getShardingParameter();
        LOG.info(String.format("ProxyJob-shard-%d handling on site %s.", item, siteParser));
        /** 难道每个分片执行一次init(),究竟什么是分布式??? */
        proxyService.init();
        try {
            proxyService.downLoadProxy(siteParser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
