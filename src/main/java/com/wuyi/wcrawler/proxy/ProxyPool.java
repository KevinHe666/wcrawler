package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;

import com.wuyi.wcrawler.proxy.monitor.cache.CacheHighLimitMonitor;
import com.wuyi.wcrawler.proxy.monitor.cache.CacheMonitor;
import com.wuyi.wcrawler.proxy.monitor.cache.CacheLowLimitMonitor;
import com.wuyi.wcrawler.proxy.monitor.cache.CacheSyncMonitor;
import com.wuyi.wcrawler.proxy.monitor.core.CoreLowLimitMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuyi5 on 2017/8/25.
 *
 * 代理池
 */
@Component
public class ProxyPool {
    private Log LOG = LogFactory.getLog(ProxyPool.class);
    public static final int monitorNum = 4;
    @Autowired
    private ProxyCache proxyCache;
    @Autowired
    private ProxyCore proxyCore;

    public ProxyPool() {

    }

    @Component(value = "proxyCache")
    public static class ProxyCache {
        public static final int PROXY_CACHE_MAX_SIZE = 32; /* 1024 */
        public static final int DEFAULT_PROXY_CACHE_HIGH_THRESHOLD =  PROXY_CACHE_MAX_SIZE / 8 * 7;
        public static final int DEFAULT_PROXY_CACHE_LOW_THRESHOLD = PROXY_CACHE_MAX_SIZE / 8;
        @Autowired
        private WProxyUtil proxyUtil;
        private LinkedList<Proxy> pCache;
        private ReentrantLock cacheLock;
        private Condition notFull;
        private Condition notEmpty;
        private Condition notLowLevel;
        private Condition notHighLevel;

        public ProxyCache() {
            pCache = new LinkedList<Proxy>();
            cacheLock = new ReentrantLock();
            notFull = cacheLock.newCondition();
            notEmpty = cacheLock.newCondition();
            notLowLevel = cacheLock.newCondition();
            notHighLevel = cacheLock.newCondition();
        }

        public int cacheSize() {
            cacheLock.lock();
            try {
                return pCache.size();
            } finally {
                cacheLock.unlock();
            }
        }

        public boolean isEmpty() {
            return cacheSize() == 0;
        }
        public boolean isFull() {
            return cacheSize() == PROXY_CACHE_MAX_SIZE;
        }

        public void add(Proxy proxy) {
            cacheLock.lock();
            try {
                while(isFull()) {
                    notFull.await();
                }
                pCache.add(proxy);
                notEmpty.signalAll();
                if(pCache.size() > DEFAULT_PROXY_CACHE_HIGH_THRESHOLD) {
                    notHighLevel.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }
        }

        public List<Proxy> get(int fillNum) {
            cacheLock.lock();
            List<Proxy> proxies = null;
            try{
                while(isEmpty()) {
                    notEmpty.await();
                }
                proxies = new ArrayList<Proxy>();
                Iterator it = pCache.iterator();
                while(proxies.size() < fillNum && it.hasNext()) {
                    proxies.add((Proxy) it.next());
                    it.remove();
                }
                notFull.signalAll();
                if(pCache.size() <= DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
                    notLowLevel.signalAll();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }
            return proxies;
        }

        public void fillCache() {
            cacheLock.lock();
            try {
                while (cacheSize() > DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
                    notLowLevel.await();
                }
                Set<Integer> ids = new HashSet<Integer>();
                for(Proxy proxy : pCache) {
                    ids.add(proxy.getId());
                }
                List<Proxy> proxies = proxyUtil.fetchProxy(CacheMonitor.PROXY_CACHE_MAX_SIZE / 2);
                for(Proxy proxy : proxies) {
                    if(!ids.contains(Integer.valueOf(proxy.getId()))) {
                        pCache.add(proxy);
                    }
                }
                notEmpty.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }
        }

        public void flushCache() {
            cacheLock.lock();
            try {
                while (pCache.size() <= DEFAULT_PROXY_CACHE_HIGH_THRESHOLD) {
                    notHighLevel.await();
                }
                List<Proxy> saveDBProxies = new ArrayList<Proxy>();
                Iterator<Proxy> it = pCache.iterator();
                while (it.hasNext()) {
                    Proxy p = it.next();
                    saveDBProxies.add(p);
                }
                proxyUtil.saveProxy(saveDBProxies);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }
        }

        public void flushTimer() {
            cacheLock.lock();
            try {
                List<Proxy> saveDBProxies = new ArrayList<Proxy>();
                Iterator<Proxy> it = pCache.iterator();
                while (it.hasNext()) {
                    Proxy p = it.next();
                    saveDBProxies.add(p);
                }
                proxyUtil.saveProxy(saveDBProxies);
            } finally {
                cacheLock.unlock();
            }
        }

    }
    @Component(value = "proxyCore")
    public static class ProxyCore {
        private static final int DEFAULT_PROXY_CORE_SIZE = 8; /* 512 */
        private static final int DEFAULT_PROXY_CORE_THRESHOLD = DEFAULT_PROXY_CORE_SIZE / 8;

        private PriorityQueue<Proxy> pCore;
        private ReentrantLock coreLock;
        private Condition notLowLevel;
        private Condition notEmpty;
        public ProxyCore() {
            pCore = new PriorityQueue<Proxy>();
            coreLock = new ReentrantLock();
            notLowLevel = coreLock.newCondition();
            notEmpty = coreLock.newCondition();
        }
        public int coreSize() {
            coreLock.lock();
            try {
                return pCore.size();
            } finally {
                coreLock.unlock();
            }
        }

        public boolean isEmpty() {
            coreLock.lock();
            try {
                return coreSize() == 0;
            } finally {
                coreLock.unlock();
            }
        }

        public boolean isFull() {
            coreLock.lock();
            try {
                return coreSize() == DEFAULT_PROXY_CORE_SIZE;
            } finally {
                coreLock.unlock();
            }
        }

        public Proxy getProxy() {
            coreLock.lock();
            Proxy proxy = null;
            try {
                while(isEmpty()) {
                    notEmpty.await();
                }
                proxy = pCore.poll();
                if(pCore.size() <= DEFAULT_PROXY_CORE_THRESHOLD) {
                    notLowLevel.signalAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                coreLock.unlock();
            }
            return proxy;
        }

        public void addProxy(List<Proxy> proxies) {
            coreLock.lock();
            try {
                for (Proxy proxy : proxies) {
                    pCore.add(proxy);
                }
            } finally {
                coreLock.unlock();
            }
        }

        public void fillCore(ProxyCache cache) {
            coreLock.lock();
            try{
                while(pCore.size() > DEFAULT_PROXY_CORE_THRESHOLD) {
                    notLowLevel.await();
                }
//                /** 一个静态内部类的私有变量在另一个静态内部类的方法中居然可见... **/
//                Iterator<Proxy> it = cache.proxyCache.iterator();

                List<Proxy> proxies = cache.get(DEFAULT_PROXY_CORE_SIZE - pCore.size());
                addProxy(proxies);
                notEmpty.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                coreLock.unlock();
            }
        }
    }

    public Proxy getProxy() {
        return proxyCore.getProxy();
    }

    public void addProxy(Proxy proxy) {
        proxyCache.add(proxy);
    }
    public int getProxyPoolSize() {
        return proxyCore.coreSize();
    }

    public boolean isEmptyProxyPool() {
        return proxyCore.isEmpty();
    }

    public boolean isFullProxyPool() {
        return proxyCore.isFull();
    }

    public int getProxyCacheSize() {
        return proxyCache.cacheSize();
    }

    public boolean isEmptyProxyCache() {
        return proxyCache.isEmpty();
    }

    public boolean isFullProxyCache() {
        return proxyCache.isFull();
    }

    public ProxyCore getProxyCore() {
        return this.proxyCore;
    }

    public ProxyCache getProxyCache() {
        return this.proxyCache;
    }

}
