package com.wuyi.wcrawler.proxy.parser;

import java.util.regex.Pattern;

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
	
	public void parse() {
		System.out.println("SITEPARSER");	
	}

	public String getFullSite(String prefix, String domain, int page) {
		if (domain == null) {
			return  prefix;
		}
		return prefix + "/" + domain + "/" + page;
	}

	public boolean isIpOk(String ip) {
		return Pattern.matches(ipPattern, ip);
	}
}
