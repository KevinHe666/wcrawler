package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.bean.Proxy;
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
 */
@Component(value = "ip181")
public class Ip181Parser extends SiteParser {
	Log LOG = LogFactory.getLog(Ip181Parser.class);
	@Autowired
	private ProxyCollector pCollector;
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
			if(isIpOk(ip)) {
				Proxy proxy = new Proxy();
				proxy.setIp(ip);
				proxy.setPort(tds.get(1).text());
				if(ProxyFilterUtil.contains(proxy)) {
					continue;
				}
//				LOG.info(proxy.getIp() + " " + proxy.getPort());
				pCollector.addProxy(proxy);
			}
		}
	}
}
