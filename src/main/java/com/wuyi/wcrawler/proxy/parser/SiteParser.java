package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.proxy.ProxyTest;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class SiteParser {
	private final int testNums = 3;
	public final String ipPattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
									+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
									+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
									+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	public String site;

	@Autowired
	private ApplicationContextUtil applicationContextUtil;

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
