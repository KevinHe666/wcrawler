package com.wuyi.wcrawler.proxy.monitor.core;

import com.wuyi.wcrawler.proxy.ProxyPool;
import com.wuyi.wcrawler.proxy.monitor.Monitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/31.
 */
@Component(value = "coreMonitor")
public class CoreMonitor extends Monitor {
    private static final int DEFAULT_PROXY_CORE_SIZE = 8; /* 512 */
    private static final int DEFAULT_PROXY_CORE_THRESHOLD = DEFAULT_PROXY_CORE_SIZE / 8;
}
