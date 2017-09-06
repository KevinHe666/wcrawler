package com.wuyi.wcrawler.crawler;

import com.wuyi.wcrawler.util.WHttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/17.
 */
@Component(value = "zhCrawler")
@Scope("prototype")
public class ZHCrawler extends Crawler {

    private static final Log LOG = LogFactory.getLog(ZHCrawler.class);

    public ZHCrawler() { super();}

    public void run() {
        crawl(this.getUrl());
    }

    @Override
    public void crawl(String url) {
        LOG.info(url);
        String peopleHtml = WHttpClientUtil.getPage(url, true);
        Document peopleDoc = Jsoup.parse(peopleHtml);
        Element a = peopleDoc.getElementsByClass("FollowshipCard-counts").first().child(0);
        String followingUrl = a.absUrl("href");
        LOG.info(followingUrl);
        String followingHtml = WHttpClientUtil.getPage(followingUrl, true);
        Document followingDoc = Jsoup.parse(followingHtml);
        Elements items = followingDoc.getElementById("Profile-following").child(1).children();
        LOG.info(items.toString());
    }

}
