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
//        int item = shardingContext.getShardingItem();
        String siteParser = shardingContext.getShardingParameter();
        LOG.info(String.format("ProxyJob handling on site %d.", siteParser));
        proxyService.downLoadProxyIp(siteParser);
    }
}
