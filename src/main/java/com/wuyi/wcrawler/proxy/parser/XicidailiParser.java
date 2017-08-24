package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.proxy.ProxySite;

public class XicidailiParser extends SiteParser {	
	public XicidailiParser() {
		this(ProxySite.XICIDAILI.getSite());
	}
	
	public XicidailiParser(String site) {
		super(site);
	}
	@Override
	public void parser() {
		System.out.println("XICIDAILI");
	}

}
