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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component(value = "mimi")
public class MimiParser extends SiteParser {
	private static Log LOG = LogFactory.getLog(MimiParser.class);
	private final int pages = 3;
	@Autowired
	private ProxyPool proxyPool;
	public MimiParser() {
		this(ProxySite.MIMIIP.getSite());
	}
	public MimiParser(String site) {
		super(site);
	}
	@Override
	public void parser() {
		LOG.info("MINIIP");
		String full_site;
		String [] domains = ProxySite.MIMIIP.getDomains();
		for(String domain : domains) {
			LOG.info(domain);
			for(int page = 1; page <= pages;  page++){
				full_site = getFullSite(this.site, domain, page);
				CloseableHttpClient httpClient = WHttpClientUtil.getHttpClient();
				String html = WHttpClientUtil.getPage(httpClient, full_site, false);
				Document doc = Jsoup.parse(html);
				Elements trs = doc.getElementsByTag("tr");
				for(Element tr : trs) {
					Elements tds = tr.children();
					String ip = tds.get(0).text();
					if(Pattern.matches(ipPattern, ip)) {
						Proxy proxy = new Proxy();
						proxy.setIp(ip);
						proxy.setPort(tds.get(1).text());
						LOG.info(proxy.getIp() + " " + proxy.getPort());
						proxyPool.addProxy(proxy);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml",
				"classpath:spring.xml");
		MimiParser xp = (MimiParser) ctx.getBean("mimi");
		xp.parser();
		LOG.info("MimiParser ended");
	}
}
