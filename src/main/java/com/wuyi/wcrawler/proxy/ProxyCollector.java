package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.entity.Proxy;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuyi5 on 2017/8/30.
 */

@Component("proxyCollector")
public class ProxyCollector {

    private Queue<Proxy> collector;
    private ReentrantLock lock;
    private Condition notEmpty;
    private Condition notFull;

    public ProxyCollector() {
        collector = new LinkedList<Proxy>();
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void addProxy(Proxy proxy) {
        lock.lock();
        try {
            while (isFull()) {
                notFull.await();
            }
            collector.add(proxy);
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Proxy getProxy() {
        lock.lock();
        Proxy proxy =null;
        try {
            while(isEmpty()) {
                notEmpty.await();
            }
            proxy = collector.poll();
            notFull.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return proxy;
    }

    public int collectorSize() {
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

    public boolean isFull() {
        lock.lock();
        try {
            return collector.size() == Integer.MAX_VALUE;
        } finally {
            lock.unlock();
        }
    }
}
