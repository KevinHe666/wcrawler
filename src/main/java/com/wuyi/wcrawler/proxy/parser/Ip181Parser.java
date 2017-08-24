package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.proxy.ProxySite;

public class Ip181Parser extends SiteParser {
	public Ip181Parser() {
		this(ProxySite.IP181.getSite());
	}
	public Ip181Parser(String site) {
		super(site);
	}
	@Override
	public void parser() {
		System.out.println("IP181");
	}

}
