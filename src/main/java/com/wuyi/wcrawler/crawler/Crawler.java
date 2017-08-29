package com.wuyi.wcrawler.crawler;

import com.wuyi.wcrawler.bean.CrawlerTask;

/**
 * Created by wuyi5 on 2017/8/17.
 */
public abstract class Crawler extends CrawlerTask implements Runnable {
    Crawler() {}
    abstract void crawl(String url);
}
