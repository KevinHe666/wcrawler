package com.wuyi.wcrawler.proxy.monitor.cache;

import com.wuyi.wcrawler.proxy.monitor.Monitor;

/**
 * Created by wuyi5 on 2017/8/30.
 */
public class CacheMonitor extends Monitor {
    public static final int PROXY_CACHE_MAX_SIZE = 32; /* 1024 */
    public static final int DEFAULT_PROXY_CACHE_HIGH_THRESHOLD =  PROXY_CACHE_MAX_SIZE / 8 * 7;
    public static final int DEFAULT_PROXY_CACHE_LOW_THRESHOLD = PROXY_CACHE_MAX_SIZE / 8;
    public CacheMonitor(String monitorName) {
        super(monitorName);
    }
}
