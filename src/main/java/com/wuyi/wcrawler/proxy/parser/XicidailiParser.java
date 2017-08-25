package com.wuyi.wcrawler.proxy.parser;

import com.wuyi.wcrawler.proxy.ProxySite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XicidailiParser extends SiteParser {
	Log LOG = LogFactory.getLog(XicidailiParser.class);
	private String[] domains;
	public XicidailiParser() {
		this(ProxySite.XICIDAILI.getSite());
	}
	
	public XicidailiParser(String site) {
		super(site);
	}

	@Override
	public void parser() {
		System.out.println("XICIDAILI");
		String full_site;
		domains = ProxySite.XICIDAILI.getDomains();
		for(String domain : domains) {
			full_site = getFullSite(this.site, domain);


		}
	}

}
