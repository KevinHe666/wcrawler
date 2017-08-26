package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.dao.ProxyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuyi5 on 2017/8/25.
 *
 * ProxyPool作为可作为请求和数据库之间的代理缓存池
 */
@Component
public class ProxyPool {
    private static final int STORE_CLEAN = 0;
    private static final int STORE_QUEUE = 1;
    private static final int STORE_DB = 2;
    private static final int DEFAULT_PROXY_QUEUE_SIZE = 512;
    private static final int DEFAULT_PROXY_QUEUE_THRESHOLD = DEFAULT_PROXY_QUEUE_SIZE / 8;
    private static final int PROXY_CACHE_MAX_SIZE = 1024;
    private static final int DEFAULT_PROXY_CACHE_HIGH_THRESHOLD =  PROXY_CACHE_MAX_SIZE / 8 * 7;
    private static final int DEFAULT_PROXY_CACHE_LOW_THRESHOLD = PROXY_CACHE_MAX_SIZE / 8;
    private final int QUEUE_EMPTY = 0;
    private final int QUEUE_FULL = DEFAULT_PROXY_QUEUE_SIZE;
    private static ProxyPool instance;
    private PriorityQueue<Proxy> proxyQueue;
    private LinkedList<Proxy> proxyCache;
    private ReentrantLock queueLock;
    private ReentrantLock cacheLock;
    private Condition cacheLowest;
    private CacheMonitor cm;
    @Autowired
    ProxyDao proxyDao;


    class CacheMonitor implements Runnable {

        public void run() {
            try {
                cacheLock.lock();
                while(proxyCache.size() > DEFAULT_PROXY_CACHE_LOW_THRESHOLD)
                    cacheLowest.await();
                fillCache();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }

        }
    }

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
        proxyQueue = new PriorityQueue<Proxy>(DEFAULT_PROXY_QUEUE_SIZE);
        proxyCache = new LinkedList<Proxy>();
        queueLock = new ReentrantLock();
        cacheLock = new ReentrantLock();
        cacheLowest = cacheLock.newCondition();
    }

    public Proxy getProxy() {
        Proxy proxy;
        try {
            queueLock.lock();
            proxy = proxyQueue.poll();
            /**
             * 如果proxyQueue的size小于阈值了,就去proxyCache取proxy
             * */
            if(getProxyPoolSize() < DEFAULT_PROXY_QUEUE_THRESHOLD) {
                fillPool();
            }
        } finally {
            queueLock.unlock();
        }

        return proxy;
    }

    public void fillPool() {
        Iterator<Proxy> it = proxyCache.iterator();
        while(!isFullProxyPool() && it.hasNext()) {
            proxyQueue.add(it.next());
            it.remove();
        }
        /**
         * 这里size()的获取没有加锁,可能有问题
         * condition不能在这儿判断,应该放到proxyCache自己的读写方法中(循环判断)
         * */
        if(proxyCache.size() < DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
            cacheLowest.signal();
        }

    }

    public void fillCache() {

    }

    public void addProxy(Proxy proxy) {

        queueLock.lock();
        if(proxy.getStoreStatus() == STORE_CLEAN && proxyQueue.size() != QUEUE_FULL) {
            proxy.setStoreStatus(STORE_QUEUE);
            proxyQueue.add(proxy);
            queueLock.unlock();

        } else {
            try {
                cacheLock.lock();
                if(proxyCache.size() < PROXY_CACHE_MAX_SIZE) {
                    proxyCache.add(proxy);
                    /**
                     * 如果,proxCache的size超过了阈值,则清理proxyCache的数据:
                     * 保留存储状态为STORE_CLEAN或STORE_DB的proxy,存储状态为STORE_QUEUE的proxy写入db
                     * */
                    if (proxyCache.size() >= DEFAULT_PROXY_CACHE_HIGH_THRESHOLD) {
                        List<Proxy> saveDBProxys = new ArrayList<Proxy>();
                        Iterator<Proxy> it = proxyCache.iterator();
                        while (it.hasNext()) {
                            Proxy p = it.next();
                            /**
                             * 如果proxy刚从proxyQueue里出来,说明它刚被使用过(成功?失败?),
                             * 则将其写入到DB中
                             * */
                            if (p.getStoreStatus() == STORE_QUEUE) {
                                p.setStoreStatus(STORE_DB);
                                saveDBProxys.add(p);
                                it.remove();
                            }
                        }
                        saveProxyToDB(saveDBProxys);
                    }
                }
            } finally {
                cacheLock.unlock();
            }
        }
    }

    public void saveProxyToDB(List<Proxy> proxies) {
        proxyDao.insert(proxies);
    }

    public int getProxyPoolSize() {
        try{
            queueLock.lock();
            return proxyQueue.size();
        } finally {
            queueLock.unlock();
        }

    }

    public boolean isEmptyProxyPool() {
        try{
            queueLock.lock();
            return proxyQueue.isEmpty();
        } finally {
            queueLock.unlock();
        }
    }

    public boolean isFullProxyPool() {
        try{
            queueLock.lock();
            return proxyQueue.size() == QUEUE_FULL;
        } finally {
            queueLock.unlock();
        }
    }

    public int getProxyCacheSize() {
        try {
            cacheLock.lock();
            return proxyCache.size();
        } finally {
            cacheLock.unlock();
        }
    }
}
