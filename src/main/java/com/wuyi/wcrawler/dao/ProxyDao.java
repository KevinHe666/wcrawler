package com.wuyi.wcrawler.dao;

import com.wuyi.wcrawler.bean.Proxy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ProxyDao {
	void insert(Proxy proxy);
	void insertAll(List<Proxy> proxys);
	List<Proxy> selectRand(@Param("limit")int limit);
	Integer count();
}
