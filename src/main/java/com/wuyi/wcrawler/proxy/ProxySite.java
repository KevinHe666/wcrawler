package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.proxy.parser.Ip181Parser;
import com.wuyi.wcrawler.proxy.parser.MimiParser;
import com.wuyi.wcrawler.proxy.parser.XicidailiParser;

public enum ProxySite {
	XICIDAILI(XicidailiParser.class, "http://www.xicidaili.com"),
	IP181(Ip181Parser.class, "http://www.ip181.com"),
	MIMIIP(MimiParser.class, "http://www.mimiip.com");
	String site;
	Class<?> clazz;
	
	private ProxySite(Class<?> clazz, String site) {
		// TODO Auto-generated constructor stub
		this.clazz = clazz;
		this.site = site;
	}
	public Class<?> getParserClass() {
		return this.clazz;
	}
	public String getSite() {
		return this.site;
	}

}
