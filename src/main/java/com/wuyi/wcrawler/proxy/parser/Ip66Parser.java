package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.entity.Proxy;
import com.wuyi.wcrawler.proxy.ProxyCollector;
import com.wuyi.wcrawler.proxy.util.ProxyFilterUtil;
import com.wuyi.wcrawler.proxy.util.ProxySite;
import com.wuyi.wcrawler.util.WHttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wuyi
 * @date 2017/10/24
 */
@Component(value = "ip66")
public class Ip66Parser extends SiteParser {

    private Log LOG = LogFactory.getLog(Ip181Parser.class);
    private static final int pages = 10;
    @Autowired
    private ProxyCollector pCollector;

    public Ip66Parser() {
        this(ProxySite.IP66.getSite());
    }
    public Ip66Parser(String site) {
        super(site);
    }
    @Override
    public void parse() {
        LOG.info("IP66");
        String fullSite;
        for (int page = 1; page <= pages; page++) {
            fullSite = this.site + page + ".html";
            String html = WHttpClientUtil.getPage(fullSite, false);
            Document doc = Jsoup.parse(html);
            Elements trs = doc.select("table tr:gt(2)");
            for (Element tr : trs) {
                Elements tds = tr.children();
                String ip = tds.get(0).text();
                if (isIpOk(ip)) {
                    Proxy proxy = new Proxy();
                    proxy.setIp(ip);
                    proxy.setPort(tds.get(1).text());
                    LOG.info("ip66 parse: " + ip + " " + tds.get(1).text());
                    if (ProxyFilterUtil.contains(proxy)) {
                        continue;
                    }
                    pCollector.addProxy(proxy);
                }
            }
        }
    }
}
