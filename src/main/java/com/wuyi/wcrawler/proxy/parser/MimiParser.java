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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component(value = "mimi")
public class MimiParser extends SiteParser {
	private static Log LOG = LogFactory.getLog(MimiParser.class);
	private final int pages = 10;
	@Autowired
	private ProxyCollector pCollector;
	public MimiParser() {
		this(ProxySite.MIMIIP.getSite());

	}
	public MimiParser(String site) {
		super(site);
	}
	@Override
	public void parse() {
		LOG.info("MINIIP");
		String fullSite;
		String [] domains = ProxySite.MIMIIP.getDomains();
		for(String domain : domains) {
			for(int page = 1; page <= pages;  page++){
				fullSite = getFullSite(this.site, domain, page);
				LOG.info(fullSite);
				String html = WHttpClientUtil.getPage(fullSite, false, false);
				Document doc = Jsoup.parse(html);
				Elements trs = doc.getElementsByTag("tr");
				for(Element tr : trs) {
					Elements tds = tr.children();
					String ip = tds.get(0).text();
					if(isIpOk(ip)) {
						Proxy proxy = new Proxy();
						proxy.setIp(ip);
						proxy.setPort(tds.get(1).text());
						if(ProxyFilterUtil.contains(proxy)) {
							continue;
						}
//						LOG.info(proxy.getIp() + " " + proxy.getPort());
						pCollector.addProxy(proxy);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml",
				"classpath:spring.xml");
		MimiParser xp = (MimiParser) ctx.getBean("mimi");
		xp.parse();
		LOG.info("MimiParser ended");
	}
}
