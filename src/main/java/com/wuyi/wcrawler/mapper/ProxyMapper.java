package com.wuyi.wcrawler.mapper;

import com.wuyi.wcrawler.entity.Proxy;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author wuyi5
 */
public interface ProxyMapper extends Mapper<Proxy> {
//	void insert(Proxy proxy);
	void insertAll(List<Proxy> proxys);
	List<Proxy> selectRand(@Param("limit")int limit);
	Integer count();
}
