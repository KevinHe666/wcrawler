package com.wuyi.wcrawler.proxy.monitor.core;

import com.wuyi.wcrawler.proxy.monitor.Monitor;

/**
 * Created by wuyi5 on 2017/8/31.
 */
public class CoreMonitor extends Monitor {
    private static final int DEFAULT_PROXY_CORE_SIZE = 8; /* 512 */
    private static final int DEFAULT_PROXY_CORE_THRESHOLD = DEFAULT_PROXY_CORE_SIZE / 8;

    private

    public CoreMonitor(String monitorName) {
        super(monitorName);
    }
}
