package com.wuyi.wcrawler.proxy.monitor.core;

import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/31.
 */
@Component(value = "coreLowLimitMonitor")
public class CoreLowLimitMonitor extends CoreMonitor implements Runnable {
    public void run() {
        while (true) {
            proxyPool.getProxyCore().fillCore(proxyPool.getProxyCache());
        }
    }
}
