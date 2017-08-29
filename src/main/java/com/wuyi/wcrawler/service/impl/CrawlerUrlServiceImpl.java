package com.wuyi.wcrawler.service.impl;

import com.wuyi.wcrawler.bean.CrawlerTask;
import com.wuyi.wcrawler.bean.CrawlerUrlBean;
import com.wuyi.wcrawler.crawler.ZHCrawler;
import com.wuyi.wcrawler.dao.CrawlerUrlDao;
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
 * Created by wuyi5 on 2017/8/17.
 */

@Service
public class CrawlerUrlServiceImpl implements CrawlerUrlService {
	private static final Log LOG = LogFactory.getLog(CrawlerUrlServiceImpl.class);
    private static final int DEFAULT_TAR_AMOUNT = 20;
	@Autowired
    private CrawlerUrlDao crawlerUrlDao;
	@Autowired
    ApplicationContextUtil applicationContextUtil;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public List<CrawlerUrlBean> fetchUrl(int shardingItem, int shardingTotalCount) {
        Map<String, Object> url_param_map = new HashMap<String, Object>();
        url_param_map.put("shardingItem", shardingItem);
        url_param_map.put("shardingTotalCount", shardingTotalCount);
        List<CrawlerUrlBean> urls = crawlerUrlDao.select(url_param_map);
        if(urls.size() > 0) {
            clockUrl((urls));
        } else {
            return null;
        }
        return urls;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void clockUrl(List<CrawlerUrlBean> urls) {
        List<Integer> ids = new ArrayList<Integer>();
        for(CrawlerUrlBean url : urls) {
            ids.add(url.getId());
        }
        crawlerUrlDao.updateUrlStatus(ids);
    }

    public void crawler(List<CrawlerUrlBean> urls) {
        /**
         * 线程池，执行爬虫任务
         * */
        int nThreads = Math.max(1, urls.size() / 2);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThreads);
        for (CrawlerUrlBean url: urls) {
            ZHCrawler zhCrawler = (ZHCrawler) applicationContextUtil.getBean("zhCrawler");
            zhCrawler.setStartTime(System.currentTimeMillis());
            zhCrawler.setStatus(CrawlerTask.CREATED);
            zhCrawler.setCurAmount(0);
            zhCrawler.setTarAmount(DEFAULT_TAR_AMOUNT);
            zhCrawler.setUrl(url.getUrl());
            fixedThreadPool.execute(zhCrawler);
        }
    }
}
