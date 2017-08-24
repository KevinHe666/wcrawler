package com.wuyi.wcrawler.crawler;

import com.wuyi.wcrawler.bean.CrawlerUrlBean;

/**
 * Created by wuyi5 on 2017/8/17.
 */
public interface Crawler {
    void run(CrawlerUrlBean url);
}
