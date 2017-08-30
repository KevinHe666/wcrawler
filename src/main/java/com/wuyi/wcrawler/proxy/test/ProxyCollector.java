package com.wuyi.wcrawler.proxy.test;

import com.wuyi.wcrawler.bean.Proxy;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuyi5 on 2017/8/30.
 */

@Component("proxyCollector")
public class ProxyCollector {

    private Queue<Proxy> collector;
    private ReentrantLock lock;

    public ProxyCollector() {
        collector = new LinkedList<Proxy>();
        lock = new ReentrantLock();
    }

    public Proxy getProxy() {
        lock.lock();
        try {
            return  collector.poll();
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        lock.lock();
        try{
            return collector.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return collector.isEmpty();
        } finally {
            lock.unlock();
        }
    }
}
