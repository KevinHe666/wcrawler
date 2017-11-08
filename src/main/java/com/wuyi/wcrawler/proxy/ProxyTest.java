package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.entity.Proxy;
import com.wuyi.wcrawler.proxy.util.WProxyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * ProxyTest的功能变更：之前ProxyTest对象用于检测下载的代理是否可用。实际使用发现，有些检测未通过的代理在实际运用中是
 * 可用的。所以，现在的流程调整为：proxy下载到collector中，proxyTest取出collector中的proxy，然后将其存储到数据库中。
 * 后期需要做的是，定期检测数据库中不可用的僵尸代理。
 * @author wuyi5
 * @date 2017/8/30
 */
@Component(value = "proxyTest")
@Scope("prototype")
public class ProxyTest implements Runnable {
    private static Log LOG = LogFactory.getLog(ProxyTest.class);
    @Autowired
    private ProxyCollector proxyCollector;
    @Autowired
    private ProxyPool proxyPool;
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Set<Proxy> proxySet = proxyCollector.getProxy();
            Iterator<Proxy> it = proxySet.iterator();
            List<Proxy> saveDBProxies = new ArrayList<Proxy>();
            while (it.hasNext()) {
                Proxy proxy = it.next();
                if (!WProxyUtil.contains(proxy)) {
                    saveDBProxies.add(proxy);
                }
            }
            WProxyUtil.saveProxy(saveDBProxies);
        }
    }
}
