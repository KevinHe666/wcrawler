package com.wuyi.wcrawler.proxy.util;

import com.wuyi.wcrawler.proxy.parser.Ip181Parser;
import com.wuyi.wcrawler.proxy.parser.MimiParser;
import com.wuyi.wcrawler.proxy.parser.XicidailiParser;

public enum ProxySite {
	XICIDAILI(XicidailiParser.class, "http://www.xicidaili.com", new String[]{"nn", "nt", "wn", "wt"}),
	IP181(Ip181Parser.class, "http://www.ip181.com", null),
	MIMIIP(MimiParser.class, "http://www.mimiip.com", new String[]{"gngao", "gnpu"}),
	KUAIDAILI(null, "http://www.kuaidaili.com/free/", null),
	IP66(null, "http://www.66ip.cn/", null),
	IP3366(null, "http://www.ip3366.net/free/", null),
	PROXY360(null, "http://www.proxy360.cn/Region/China", null),
	DATA5U(null, "http://www.data5u.com/free/index.shtml", null),
	KXDAILI(null, "http://www.kxdaili.com/", null);

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
