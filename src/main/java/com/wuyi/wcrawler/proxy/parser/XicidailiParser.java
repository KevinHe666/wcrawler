package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.proxy.ProxyPool;
import com.wuyi.wcrawler.proxy.ProxySite;
import com.wuyi.wcrawler.proxy.WProxyUtil;
import com.wuyi.wcrawler.util.WHttpClientUtil;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

public class XicidailiParser extends SiteParser {
	Log LOG = LogFactory.getLog(XicidailiParser.class);
	private ProxyPool proxyPool;
	public XicidailiParser() {
		this(ProxySite.XICIDAILI.getSite());
	}
	
	public XicidailiParser(String site) {
		super(site);
		proxyPool = ProxyPool.getInstance();
	}

	@Override
	public void parser() {
		System.out.println("XICIDAILI");
		String full_site;
		String [] domains = ProxySite.XICIDAILI.getDomains();
		for(String domain : domains) {
			full_site = getFullSite(this.site, domain);
			CloseableHttpClient httpClient = WHttpClientUtil.getHttpClient();
			String html = WHttpClientUtil.getPage(httpClient, full_site, false);
			Document doc = Jsoup.parse(html);
			Elements trs = doc.getElementsByTag("tr");
			for(Element tr : trs) {
				Elements tds = tr.children();
				String ip = tds.get(1).text();
				if(Pattern.matches(ipPattern, ip)) {
					Proxy proxy = new Proxy();
					proxy.setIp(ip);
					proxy.setPort(tds.get(2).text());
					proxyPool.addProxy(proxy);
					System.out.println(proxy.getIp() + " " + proxy.getPort());
				}
			}
		}
	}
	
	public static void main(String[] args) {
		XicidailiParser xp = new XicidailiParser();
		xp.parser();
	}

}
