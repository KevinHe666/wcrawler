package com.wuyi.wcrawler.proxy.parser;

public class SiteParser {
	public String site;
	public SiteParser() {
		
	}
	public SiteParser(String site) {
		this.site = site;
	}
	
	public void parser() {
		System.out.println("SITEPARSER");	
	}

	public String getFullSite(String prefix, String domain) {
		if (domain == null) {
			return  prefix;
		}
		return prefix + "/" + domain;
	}
}
