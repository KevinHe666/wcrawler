package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;

import com.wuyi.wcrawler.proxy.monitor.cache.CacheMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuyi5 on 2017/8/25.
 *
 * ProxyPool作为可作为请求和数据库之间的代理缓存池
 */
@Component
public class ProxyPool {
    private Log LOG = LogFactory.getLog(ProxyPool.class);
	private static final int STORE_CLEAN = 0;
    private static final int STORE_CORE = 1;
    private static final int STORE_DB = 2;
//    private static final int DEFAULT_PROXY_CORE_SIZE = 8; /* 512 */
//    private static final int DEFAULT_PROXY_CORE_THRESHOLD = DEFAULT_PROXY_CORE_SIZE / 8;

//    private final int CORE_EMPTY = 0;
//    private final int CORE_FULL = DEFAULT_PROXY_CORE_SIZE;
//    private PriorityQueue<Proxy> proxyCore;
//    private LinkedList<Proxy> proxyCache;
//    private ReentrantLock queueLock;
//    private ReentrantLock cacheLock;

//    @Autowired
//    private WProxyUtil proxyUtil;

    @Autowired
    private ProxyCache proxyCache;
    @Autowired
    private ProxyCore proxyCore;
    @Component(value = "proxyCache")
    public static class ProxyCache {
        public static final int PROXY_CACHE_MAX_SIZE = 32; /* 1024 */
        public static final int DEFAULT_PROXY_CACHE_HIGH_THRESHOLD =  PROXY_CACHE_MAX_SIZE / 8 * 7;
        public static final int DEFAULT_PROXY_CACHE_LOW_THRESHOLD = PROXY_CACHE_MAX_SIZE / 8;
        private LinkedList<Proxy> proxyCache;
        private ReentrantLock cacheLock;
        private Condition notFull;
        private Condition notEmpty;

        public ProxyCache(){
            proxyCache = new LinkedList<Proxy>();
            cacheLock = new ReentrantLock();
            notFull = cacheLock.newCondition();
            notEmpty = cacheLock.newCondition();
        }

        public int size() {
            cacheLock.lock();
            try {
                return proxyCache.size();
            } finally {
                cacheLock.unlock();
            }
        }

        public boolean isEmpty() {
            return size() == 0;
        }
        public boolean isFull() {
            return size() == PROXY_CACHE_MAX_SIZE;
        }

        public void add(Proxy proxy) {
            cacheLock.lock();
            try {
                while(isFull()) {
                    notFull.await();
                }
                proxyCache.add(proxy);
                notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }
        }

        public List<Proxy> get(int num) {
            cacheLock.lock();
            List<Proxy> proxies = null;
            try{
                while(isEmpty()) {
                    notEmpty.await();
                }
                proxies = new ArrayList<Proxy>();
                Iterator it = proxyCache.iterator();
                while(proxies.size() < num && it.hasNext()) {
                    proxies.add((Proxy) it.next());
                    it.remove();
                }
                notFull.signal();
                if(proxyCache.size() < DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
                    /************/
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cacheLock.unlock();
            }
            return proxies;
        }
    }
    @Component(value = "proxyCore")
    public static class ProxyCore {
        private static final int DEFAULT_PROXY_CORE_SIZE = 8; /* 512 */
        private static final int DEFAULT_PROXY_CORE_THRESHOLD = DEFAULT_PROXY_CORE_SIZE / 8;

        private PriorityQueue<Proxy> proxyCore;
        private ReentrantLock coreLock;
        private Condition notLowLevel;
        private Condition notEmpty;
        public ProxyCore() {
            proxyCore = new PriorityQueue<Proxy>();
            coreLock = new ReentrantLock();
            notLowLevel = coreLock.newCondition();
            notEmpty = coreLock.newCondition();
        }
        public int size() {
            coreLock.lock();
            try {
                return proxyCore.size();
            } finally {
                coreLock.unlock();
            }
        }

//        public boolean isLowLevel

        public boolean isEmpty() {
            coreLock.lock();
            try {
                return size() == 0;
            } finally {
                coreLock.unlock();
            }
        }

        public boolean isFull() {
            coreLock.lock();
            try {
                return size() == DEFAULT_PROXY_CORE_SIZE;
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
                proxy = proxyCore.poll();
                if(proxyCore.size() <= DEFAULT_PROXY_CORE_THRESHOLD) {
                    notLowLevel.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                coreLock.unlock();
            }
            return proxy;
        }

        public void fillCore(ProxyCache cache) {
            coreLock.lock();
            try{
                while(proxyCore.size() > DEFAULT_PROXY_CORE_THRESHOLD) {
                    notLowLevel.await();
                }
//                /** 一个静态内部类的私有变量在另一个静态内部类的方法中居然可见... **/
//                Iterator<Proxy> it = cache.proxyCache.iterator();
                int fillNum = DEFAULT_PROXY_CORE_SIZE - proxyCore.size();
                List<Proxy> proxies = cache.get(fillNum);
                addProxy(proxies);
                notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                coreLock.unlock();
            }
        }

        public void addProxy(List<Proxy> proxies) {
            coreLock.lock();
            try {
                for (Proxy proxy : proxies) {
                    proxyCore.add(proxy);
                }
            } finally {
                coreLock.unlock();
            }
        }
    }

    public void fillPool() {
		cacheLock.lock();
    	try{
            Iterator<Proxy> it = proxyCache.iterator();
            while(!isFullProxyPool() && it.hasNext()) {
                proxyCore.add(it.next());
                it.remove();
            }
            if(proxyCache.size() < CacheMonitor.DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
                cacheLowest.signal();
            }
    	} finally {
			cacheLock.unlock();
		}
    	
    }

    public void fillCache() {
        Set<Integer> ids = new HashSet<Integer>();
        for(Proxy proxy : proxyCache) {
            ids.add(proxy.getId());
        }
	    List<Proxy> proxies = proxyUtil.fetchProxy(CacheMonitor.PROXY_CACHE_MAX_SIZE / 2);
	    	for(Proxy proxy : proxies) {
	    		if(!ids.contains(Integer.valueOf(proxy.getId()))) {
	    			proxyCache.add(proxy);
	    		}
	    	}
	    	
    }
    
    public void flushCache() {
    	List<Proxy> saveDBProxies = new ArrayList<Proxy>();
        Iterator<Proxy> it = proxyCache.iterator();
        while (it.hasNext()) {
            Proxy p = it.next();
            saveDBProxies.add(p);
        }
        proxyUtil.saveProxy(saveDBProxies);
    }

    public void addProxy(Proxy proxy) {
        proxyCache.add(proxy);
    }

    public int getProxyPoolSize() {
        try{
            queueLock.lock();
            return proxyCore.size();
        } finally {
            queueLock.unlock();
        }

    }

    public boolean isEmptyProxyPool() {
        try{
            queueLock.lock();
            return proxyCore.isEmpty();
        } finally {
            queueLock.unlock();
        }
    }

    public boolean isFullProxyPool() {
        try{
            queueLock.lock();
            return proxyCore.size() == CORE_FULL;
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
