package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.proxy.ProxyPool;
import com.wuyi.wcrawler.proxy.ProxySite;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
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

@Component(value = "ip181")
public class Ip181Parser extends SiteParser {
	Log LOG = LogFactory.getLog(Ip181Parser.class);
	@Autowired
	private ProxyPool proxyPool;
	public Ip181Parser() {
		this(ProxySite.IP181.getSite());
	}
	public Ip181Parser(String site) {
		super(site);
	}
	@Override
	public void parse() {
		LOG.info("IP181");
		String fullSite = getFullSite(this.site, null, 0);
		LOG.info(fullSite);
		String html = WHttpClientUtil.getPage(fullSite, false);
		Document doc = Jsoup.parse(html);
		Elements trs = doc.getElementsByTag("tr");
		for(Element tr : trs) {
			Elements tds = tr.children();
			String ip = tds.get(0).text();
			LOG.info(ip);
			if(isIpOk(ip)) {
				Proxy proxy = new Proxy();
				proxy.setIp(ip);
				proxy.setPort(tds.get(1).text());
//				LOG.info(proxy.getIp() + " " + proxy.getPort());
				proxyPool.addProxy(proxy);
			}
		}
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml", "classpath:elastic-job-lite.xml",
				"spring.xml");
		Ip181Parser ip = (Ip181Parser) ctx.getBean("ip181");
		ip.parse();
	}
}
