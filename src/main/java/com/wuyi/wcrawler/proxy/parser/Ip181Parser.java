package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.proxy.ProxySite;

public class Ip181Parser extends SiteParser {
	private String [] domains;
	public Ip181Parser() {
		this(ProxySite.IP181.getSite());
	}
	public Ip181Parser(String site) {
		super(site);
	}
	@Override
	public void parser() {
		System.out.println("IP181");
		String full_site;
		domains = ProxySite.IP181.getDomains();
		if(domains == null || domains.length == 0) {
			full_site = getFullSite(this.site, null);
		}

	}

}
