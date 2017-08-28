package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.proxy.ProxyPool;
import com.wuyi.wcrawler.proxy.ProxySite;
import com.wuyi.wcrawler.util.WHttpClientUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component(value = "xicidaili")
public class XicidailiParser extends SiteParser {
	private static Log LOG = LogFactory.getLog(XicidailiParser.class);
	private final int pages = 10;
	@Autowired
	private ProxyPool proxyPool;
	public XicidailiParser() {
		this(ProxySite.XICIDAILI.getSite());
	}
	
	public XicidailiParser(String site) {
		super(site);
//		proxyPool = ProxyPool.getInstance();
	}

	@Override
	public void parse() {
		LOG.info("XICIDAILI");
		String fullSite;
		String [] domains = ProxySite.XICIDAILI.getDomains();
		for(String domain : domains) {
			LOG.info(domain);
			for(int page = 1; page <= pages;  page++){
				fullSite = getFullSite(this.site, domain, page);
				CloseableHttpClient httpClient = WHttpClientUtil.getHttpClient();
				String html = WHttpClientUtil.getPage(httpClient, fullSite, false);
				Document doc = Jsoup.parse(html);
				Elements trs = doc.getElementsByTag("tr");
				for(Element tr : trs) {
					Elements tds = tr.children();
					String ip = tds.get(1).text();
					if(isIpOk(ip)) {
						Proxy proxy = new Proxy();
						proxy.setIp(ip);
						proxy.setPort(tds.get(2).text());
//						LOG.info(proxy.getIp() + " " + proxy.getPort());
						proxyPool.addProxy(proxy);
					}
				}
			}
		}
	}
}
