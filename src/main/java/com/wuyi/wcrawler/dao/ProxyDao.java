package com.wuyi.wcrawler.dao;

import com.wuyi.wcrawler.bean.Proxy;

import java.util.List;

public interface ProxyDao {
	void insert(Proxy proxy);
	void insert(List<Proxy> proxys);
	void select();
}
