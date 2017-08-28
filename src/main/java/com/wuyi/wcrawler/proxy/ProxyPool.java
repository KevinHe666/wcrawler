package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.dao.ProxyDao;

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
    private static final int STORE_QUEUE = 1;
    private static final int STORE_DB = 2;
    private static final int DEFAULT_PROXY_QUEUE_SIZE = 8; /* 512 */
    private static final int DEFAULT_PROXY_QUEUE_THRESHOLD = DEFAULT_PROXY_QUEUE_SIZE / 8;
    private static final int PROXY_CACHE_MAX_SIZE = 32; /* 1024 */
    private static final int DEFAULT_PROXY_CACHE_HIGH_THRESHOLD =  PROXY_CACHE_MAX_SIZE / 8 * 7;
    private static final int DEFAULT_PROXY_CACHE_LOW_THRESHOLD = PROXY_CACHE_MAX_SIZE / 8;
    private final int QUEUE_EMPTY = 0;
    private final int QUEUE_FULL = DEFAULT_PROXY_QUEUE_SIZE;
    private PriorityQueue<Proxy> proxyQueue;
    private LinkedList<Proxy> proxyCache;
    private ReentrantLock queueLock;
    private ReentrantLock cacheLock;
    private Condition cacheLowest;
    @Autowired
    private WProxyUtil proxyUtil;

    class CacheMonitor implements Runnable {

        public void run() {
        		/**
        		 * 线程刚启动时，要等第一次proxyCache达到上限阈值时，才能再监控下限阈值
        		 * */
        		while(proxyCache.size() < DEFAULT_PROXY_CACHE_HIGH_THRESHOLD) {}
	        	while(true) {
	        		cacheLock.lock();
	        		   /**
	        		    * 如果没有上面的while，这里有个bug 线程刚启动的时候，while条件肯定不成立啦，然后就会fillCache，但是逻辑是一开始不能去数据库里读
	        		    * */
	                while(proxyCache.size() > DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
						try {
							cacheLowest.await();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	                fillCache();
	                cacheLock.unlock();
	        	}
        }
    }

    public ProxyPool() {
        proxyQueue = new PriorityQueue<Proxy>(DEFAULT_PROXY_QUEUE_SIZE);
        proxyCache = new LinkedList<Proxy>();
        queueLock = new ReentrantLock();
        cacheLock = new ReentrantLock();
        cacheLowest = cacheLock.newCondition();
        new Thread(new CacheMonitor()).start();
    }

    public Proxy getProxy() {
        Proxy proxy;
        queueLock.lock();
        try {
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
		cacheLock.lock();
    	try{
            Iterator<Proxy> it = proxyCache.iterator();
            while(!isFullProxyPool() && it.hasNext()) {
                proxyQueue.add(it.next());
                it.remove();
            }
            if(proxyCache.size() < DEFAULT_PROXY_CACHE_LOW_THRESHOLD) {
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
	    List<Proxy> proxies = proxyUtil.fetchProxy(PROXY_CACHE_MAX_SIZE / 2);
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
            /**
             * 如果proxy刚从proxyQueue里出来,说明它刚被使用过(成功?失败?),
             * 则将其写入到DB中
             * */
//            if (p.getStoreStatus() == STORE_QUEUE) {
//                p.setStoreStatus(STORE_DB);
//                saveDBProxies.add(p);
//                it.remove();
//            }
            /***************************测试******************************/
            saveDBProxies.add(p);
        }
        proxyUtil.saveProxy(saveDBProxies);
    }

    public void addProxy(Proxy proxy) {
        queueLock.lock();
        if(proxy.getStoreStatus() == STORE_CLEAN && proxyQueue.size() != QUEUE_FULL) {
            proxy.setStoreStatus(STORE_QUEUE);
            proxyQueue.add(proxy);
            /**
             * if条件里的unlock, 写法好奇怪
             * */
            queueLock.unlock(); 

        } else {
        	/**
             * else条件里的unlock， 写法好奇怪
             * */
        	queueLock.unlock();
        	
            cacheLock.lock();
            try {
                if(proxyCache.size() < PROXY_CACHE_MAX_SIZE) {
                    proxyCache.add(proxy);
                    /**
                     * 如果,proxCache的size超过了阈值,则清理proxyCache的数据:
                     * 保留存储状态为STORE_CLEAN或STORE_DB的proxy,存储状态为STORE_QUEUE的proxy写入db
                     * */
                    if (proxyCache.size() >= DEFAULT_PROXY_CACHE_HIGH_THRESHOLD) {
                        LOG.info(proxyCache.size() + "--flushCache");
                        flushCache();
                    }
                }
            } finally {
                cacheLock.unlock();
            }
        }
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
