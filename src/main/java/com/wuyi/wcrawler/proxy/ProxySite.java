package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.proxy.parser.Ip181Parser;
import com.wuyi.wcrawler.proxy.parser.MimiParser;
import com.wuyi.wcrawler.proxy.parser.XicidailiParser;

public enum ProxySite {
	XICIDAILI(XicidailiParser.class, "http://www.xicidaili.com", new String[]{"nn", "nt", "wn", "wt"}),
	IP181(Ip181Parser.class, "http://www.ip181.com", null),
	MIMIIP(MimiParser.class, "http://www.mimiip.com", new String[]{"gngao", "gnpu"});

	private Class<?> clazz;
	private String site;
	private String[] domains;
	
	ProxySite(Class<?> clazz, String site, String[] domains) {
		// TODO Auto-generated constructor stub
		this.clazz = clazz;
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
}
