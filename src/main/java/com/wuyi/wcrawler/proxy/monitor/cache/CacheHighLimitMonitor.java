package com.wuyi.wcrawler.proxy.monitor.cache;

import org.springframework.stereotype.Component;

@Component(value = "cacheHighLimitMonitor")
public class CacheHighLimitMonitor extends CacheMonitor implements Runnable {

    public void run() {
        while(true) {
            proxyPool.getProxyCache().flushCache();
        }
    }
}
