package com.wuyi.wcrawler.proxy.util;

import com.wuyi.wcrawler.Config;
import com.wuyi.wcrawler.entity.Proxy;
import com.wuyi.wcrawler.mapper.ProxyMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

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
		switch (Config.getInstance().getProxySelectPolicy()) {
			case SEQUENCE:
				return proxyMapper.selectBySequence(limit);
			case RANDOM:
				return proxyMapper.selectByRand(limit);
			case SUCCESS_PROBABILITY_PRIORITY:
				return proxyMapper.selectBySuccessProbabilityPriority(limit);
			case SUCCESS_TIMES_PRIORITY:
				return proxyMapper.selectBySuccessTimesPriority(limit);
			case SUCCESS_TIME_CONSUME_LEAST_PRIORITY:
				return proxyMapper.selectBySuccessTimeConsumeLeastPriority(limit);
			default:
				return proxyMapper.selectByRand(limit);
		}
	}

	public static boolean contains(Proxy proxy) {
		Example example = new Example(Proxy.class);
		example.createCriteria().andEqualTo("ip", proxy.getIp()).andEqualTo("port", proxy.getPort());
		List<Proxy> existProxies = proxyMapper.selectByExample(example);
		if (existProxies != null && existProxies.size() > 0) {
			return false;
		}
		return true;
	}

	public static int countProxy() {
		return proxyMapper.count().intValue();
	}
}
