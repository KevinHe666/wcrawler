package com.wuyi.wcrawler.crawler;

import com.wuyi.wcrawler.bean.CrawlerUrlBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by wuyi5 on 2017/8/17.
 */
public class UrlCrawler implements Crawler {

    private static final Log LOG = LogFactory.getLog(UrlCrawler.class);
    static {
        System.setProperty("javax.net.ssl.trustStore", "D:\\workspace\\IdeaProjects\\wcrawler\\src\\main\\resources\\zhihu.jks");
    }
    public void run(CrawlerUrlBean url) {
        LOG.info(url);
        Document home_page = null, following_page = null;
        try {
            home_page = Jsoup.connect(url.getUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element a = home_page.getElementsByClass("FollowshipCard-counts").first().child(0);
        String following_url = a.absUrl("href");
        LOG.info(following_url);
        try {
            following_page = Jsoup.connect(following_url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements items = following_page.getElementById("Profile-following").child(1).children();
        LOG.info(items.toString());

    }
}
