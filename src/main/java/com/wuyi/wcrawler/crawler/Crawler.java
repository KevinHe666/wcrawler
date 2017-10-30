package com.wuyi.wcrawler.crawler;

import com.wuyi.wcrawler.entity.CrawlerTask;

/**
 * Created by wuyi5 on 2017/8/17.
 */
public abstract class Crawler extends CrawlerTask implements Runnable {
        private String url = "http://localhost:8080/config/api/v4/members/urlToken/followees";
//    private String url = "https://www.zhihu.com/api/v4/members/urlToken/followees";
    Crawler() {}
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    abstract void crawl(String url);
}
