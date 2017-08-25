package com.wuyi.wcrawler.proxy.parser;

public class SiteParser {
	public final String ipPattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
									+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
									+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
									+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
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
