package com.wuyi.wcrawler.proxy.monitor.cache;

import org.springframework.stereotype.Component;

@Component(value = "cacheLowLimitMonitor")
public class CacheLowLimitMonitor extends CacheMonitor implements Runnable {

    @Override
    public void run() {
        while(true) {
            proxyPool.getProxyCache().fillCache();
        }
    }
}
