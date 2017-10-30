package com.wuyi.wcrawler.service.impl;

import com.wuyi.wcrawler.Config;
import com.wuyi.wcrawler.entity.CrawlerTask;
import com.wuyi.wcrawler.crawler.ZhCrawler;
import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.mapper.CrawlerUrlMapper;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.service.CrawlerUrlService;

import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author wuyi5
 * @date 2017/8/17
 */

@Service
public class CrawlerUrlServiceImpl implements CrawlerUrlService {
	private static final Log LOG = LogFactory.getLog(CrawlerUrlServiceImpl.class);
    private static final int DEFAULT_TAR_AMOUNT = 20;
    @Autowired
    private ZhUserMapper zhUserMapper;
	@Autowired
    private CrawlerUrlMapper crawlerUrlDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public List<ZhUser> fetchUrl(int shardingItem, int shardingTotalCount) {
        Map<String, Object> urlParamMap = new HashMap<String, Object>();
        urlParamMap.put("shardingItem", shardingItem);
        urlParamMap.put("shardingTotalCount", shardingTotalCount);

        List<ZhUser> users = zhUserMapper.selectBySharding(urlParamMap);
        if(users.size() > 0) {
            clockUrl(users);
        } else {
            return null;
        }
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void clockUrl(List<ZhUser> users) {
        List<Integer> ids = new ArrayList<Integer>();
        for(ZhUser user : users) {
            ids.add(user.getId());
        }
        zhUserMapper.updateUserStatus(ids);
    }

    @Override
    public void crawler(List<ZhUser> users) {
        /**
         * 线程池，执行爬虫任务
         * */
        int nThreads = Math.max(1, users.size() / 2);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (ZhUser user: users) {
            ZhCrawler zhCrawler = (ZhCrawler) ApplicationContextUtil.getBean("zhCrawler");
            zhCrawler.setStartTime(System.currentTimeMillis());
            zhCrawler.setStatus(CrawlerTask.CREATED);
            zhCrawler.setTarAmount(DEFAULT_TAR_AMOUNT);
            zhCrawler.setUrl(zhCrawler.getUrl().replace("urlToken", user.getUrlToken()));
            zhCrawler.setUrlToken(user.getUrlToken());
            executorService.execute(zhCrawler);
            if (zhUserMapper.select(null).size() > Config.newInstance().tarAmount) {
                executorService.shutdownNow();
            }
        }
    }
}
