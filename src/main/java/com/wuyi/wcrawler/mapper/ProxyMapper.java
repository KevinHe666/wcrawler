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
	List<Proxy> selectBySequence(@Param("limit") int limit);
	List<Proxy> selectByRand(@Param("limit")int limit);
	List<Proxy> selectBySuccessProbabilityPriority(@Param("limit")int limit);
	List<Proxy> selectBySuccessTimesPriority(@Param("limit")int limit);
	List<Proxy> selectBySuccessTimeConsumeLeastPriority(@Param("limit")int limit);
	Integer count();
}
