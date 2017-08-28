package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.proxy.parser.Ip181Parser;
import com.wuyi.wcrawler.proxy.parser.MimiParser;
import com.wuyi.wcrawler.proxy.parser.XicidailiParser;

public enum ProxySite {
	XICIDAILI(XicidailiParser.class, "XICIDAILI", "http://www.xicidaili.com", new String[]{"nn", "nt", "wn", "wt"}),
	IP181(Ip181Parser.class, "IP181", "http://www.ip181.com", null),
	MIMIIP(MimiParser.class, "MIMIIP", "http://www.mimiip.com", new String[]{"gngao", "gnpu"});

	public Class<?> clazz;
	public String title;
	public String site;
	public String[] domains;
	
	private ProxySite(Class<?> clazz, String title, String site, String[] domains) {
		// TODO Auto-generated constructor stub
		this.clazz = clazz;
		this.title = title;
		this.site = site;
		this.domains = domains;
	}
	public Class<?> getParserClass() {
		return this.clazz;
	}
	public String getSite() {
		return this.site;
	}
	public String[] getDomains() {
		return this.domains;
	}
	public String getTitle() {
		return title;
	}
}
