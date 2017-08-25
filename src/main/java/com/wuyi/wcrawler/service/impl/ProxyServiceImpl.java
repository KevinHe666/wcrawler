package com.wuyi.wcrawler.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.wuyi.wcrawler.proxy.ProxySite;
import com.wuyi.wcrawler.proxy.parser.SiteParser;
import com.wuyi.wcrawler.service.ProxyService;

public class ProxyServiceImpl implements ProxyService {

	public void downLoadProxyIp() {
		// TODO Auto-generated method stub
		for(ProxySite proxySite : ProxySite.values()) {
			Constructor<?> constructor;
			try {
				constructor = proxySite.getParserClass().getConstructor(String.class);
				try {
					SiteParser parser = (SiteParser) constructor.newInstance(proxySite.getSite());
					parser.parser();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void test() {
		downLoadProxyIp();
	}
	public static void main(String[] args) {
		ProxyServiceImpl proxyServiceImpl = new ProxyServiceImpl();
		proxyServiceImpl.test();
	}
}
