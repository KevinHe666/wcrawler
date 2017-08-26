package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.dao.ProxyDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WProxyUtil {
	@Autowired
	private static ProxyPool proxyPool;
	@Autowired
	private ProxyDao proxyDao;
	private static Log LOG = LogFactory.getLog(WProxyUtil.class);
	private static final String[] proxy_sites = {
			"http://www.xicidaili.com",
			"http://www.ip181.com",
			"http://www.mimiip.com"
			};

	public static Proxy getProxy() {
		return proxyPool.getProxy();
	}

	public static void save(Proxy proxy) {
		proxyPool.addProxy(proxy);
	}
	
}
