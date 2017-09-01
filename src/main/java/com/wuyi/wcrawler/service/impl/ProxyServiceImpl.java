package com.wuyi.wcrawler.service.impl;

import com.wuyi.wcrawler.proxy.ProxyTest;
import com.wuyi.wcrawler.proxy.monitor.cache.CacheHighLimitMonitor;
import com.wuyi.wcrawler.proxy.monitor.cache.CacheLowLimitMonitor;
import com.wuyi.wcrawler.proxy.monitor.cache.CacheSyncMonitor;
import com.wuyi.wcrawler.proxy.monitor.core.CoreLowLimitMonitor;
import com.wuyi.wcrawler.proxy.parser.SiteParser;
import com.wuyi.wcrawler.service.ProxyService;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProxyServiceImpl implements ProxyService {
	private Log LOG = LogFactory.getLog(ProxyServiceImpl.class);
	private static final int monitorNum = 4;
	private static final int testNum = 4;

	@Autowired
	ApplicationContextUtil applicationContextUtil;
	@Autowired
	private CoreLowLimitMonitor coreLowLimitMonitor;
	@Autowired
	private CacheLowLimitMonitor cacheLowLimitMonitor;
	@Autowired
	private CacheHighLimitMonitor cacheHighLimitMonitor;
	@Autowired
	private CacheSyncMonitor cacheSyncMonitor;

	private ExecutorService threadPool;
	public void init() {
		threadPool = Executors.newFixedThreadPool(monitorNum + testNum);
		monitorTask();
		testTask();
	}

	private void monitorTask() {
		threadPool.execute(coreLowLimitMonitor);
		threadPool.execute(cacheLowLimitMonitor);
		threadPool.execute(cacheHighLimitMonitor);
		threadPool.execute(cacheSyncMonitor);
	}

	private void testTask() {
		for(int i = 0; i < testNum; i++) {
			ProxyTest pt = (ProxyTest) applicationContextUtil.getBean("proxyTest");
			threadPool.execute(pt);
		}
	}

	public void downLoadProxy(String siteParser) {
		LOG.info(siteParser + " downloading...");
		SiteParser sp = (SiteParser) applicationContextUtil.getBean(siteParser);
		sp.parse();
	}

}
