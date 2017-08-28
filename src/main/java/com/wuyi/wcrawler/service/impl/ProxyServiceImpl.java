package com.wuyi.wcrawler.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.wuyi.wcrawler.proxy.ProxySite;
import com.wuyi.wcrawler.proxy.parser.SiteParser;
import com.wuyi.wcrawler.service.ProxyService;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ProxyServiceImpl implements ProxyService {
	private Log LOG = LogFactory.getLog(ProxyServiceImpl.class);
	@Autowired
	ApplicationContextUtil applicationContextUtil;
	public void downLoadProxyIp(String siteParser) {
		SiteParser sp = (SiteParser) applicationContextUtil.getBean(siteParser);
		sp.parser();
	}
}
