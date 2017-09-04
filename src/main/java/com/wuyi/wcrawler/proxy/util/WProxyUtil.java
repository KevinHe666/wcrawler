package com.wuyi.wcrawler.proxy.util;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.dao.ProxyDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WProxyUtil {
	private static Log LOG = LogFactory.getLog(WProxyUtil.class);

	@Autowired
	private ProxyDao proxyDao;

	public void saveProxy(List<Proxy> proxies) {
		if(proxies.size() == 1) {
			proxyDao.insert(proxies.get(0));
		} else {
			proxyDao.insertAll(proxies);
		}
	}
	public List<Proxy> fetchProxy(int limit) {
		return proxyDao.selectRand(limit);
	}
}
