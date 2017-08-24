package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.proxy.ProxySite;

public class MimiParser extends SiteParser {
	public MimiParser() {
		this(ProxySite.MIMIIP.getSite());
	}
	public MimiParser(String site) {
		super(site);
	}
	@Override
	public void parser() {
		System.out.println("MINIIP");
	}
}
