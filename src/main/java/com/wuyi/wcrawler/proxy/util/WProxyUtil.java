package com.wuyi.wcrawler.proxy.util;

import com.wuyi.wcrawler.entity.Proxy;
import com.wuyi.wcrawler.mapper.ProxyMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "wProxyUtil")
public class WProxyUtil {
	private static Log LOG = LogFactory.getLog(WProxyUtil.class);

	private static ProxyMapper proxyMapper;

	@Autowired
	public  void setProxyDao(ProxyMapper proxyDao) {
		WProxyUtil.proxyMapper = proxyDao;
	}

	public static void saveProxy(List<Proxy> proxies) {
		if(proxies.size() == 1) {
			proxyMapper.insert(proxies.get(0));
		} else {
			proxyMapper.insertAll(proxies);
		}
	}
	public static List<Proxy> fetchProxy(int limit) {
		return proxyMapper.selectRand(limit);
	}

	public static int countProxy() {
		return proxyMapper.count().intValue();
	}
}
