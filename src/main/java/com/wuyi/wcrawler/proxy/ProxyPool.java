package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;
import org.springframework.stereotype.Component;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by wuyi5 on 2017/8/25.
 *
 * ProxyPool作为可作为请求和数据库之间的代理缓存
 */
@Component
public class ProxyPool {
    private final int DEFAULT_PROXY_POOL_SIZE = 512;
    private final int DEFAULT_PROXY_POOL_THRESHOLD = 128;
    private PriorityBlockingQueue<Proxy> proxyQueue;
    private final int EMPTY = 0;
    private final int FULL = DEFAULT_PROXY_POOL_SIZE;
    private static ProxyPool instance;
    public static ProxyPool getInstance() {
        if(instance == null) {
            synchronized (ProxyPool.class) {
                if(instance == null) {
                    instance = new ProxyPool();
                }
            }
        }
        return instance;
    }
    private ProxyPool() {
        proxyQueue = new PriorityBlockingQueue<Proxy>(DEFAULT_PROXY_POOL_SIZE);
    }

    public Proxy getProxy() {
        return proxyQueue.poll();
    }
    public void addProxy(Proxy proxy) {
    		proxyQueue.add(proxy);
    }

    public int getProxyPoolSize() {
        return proxyQueue.size();
    }

    public boolean isEmptyProxyPool() {
        return proxyQueue.isEmpty();
    }

    public boolean isFullProxyPool() {
        return  proxyQueue.size() == FULL;
    }

    public boolean isExceedThreshold() {
        return proxyQueue.size() > DEFAULT_PROXY_POOL_THRESHOLD;
    }

}
