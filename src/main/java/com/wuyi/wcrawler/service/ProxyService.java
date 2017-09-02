package com.wuyi.wcrawler.service;

public interface ProxyService {
	public void init();
	public void downLoadProxy (String siteParser) throws InterruptedException;
}
