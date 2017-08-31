package com.wuyi.wcrawler.proxy.monitor.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuyi5 on 2017/8/30.
 */

@Component(value = "cacheSyncMonitor")
public class CacheSyncMonitor extends CacheMonitor implements Runnable {

    public void run() {
        while(true) {
            try {
                TimeUnit.MINUTES.sleep(5);
                proxyPool.getProxyCache().flushTimer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
