package com.wuyi.wcrawler.proxy.test;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.proxy.ProxyPool;
import com.wuyi.wcrawler.util.WHttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/30.
 */
@Component("proxyTest")
public class ProxyTest implements Runnable {
    @Autowired
    ProxyCollector proxyCollector;
    @Autowired
    ProxyPool proxyPool;
    public void run() {
        /** 循环检测下载的代理是否可用 */
        while(true) {
            while(!proxyCollector.isEmpty()) {
                Proxy proxy = proxyCollector.getProxy();
                if(testProxy(ProxyTestSite.SITE0.SITE, proxy)) {
                    /** 修改addProxy方法 */
                    proxyPool.addProxy(proxy);
                }
            }
        }
    }

    public boolean testProxy(String site, Proxy proxy) {
        String html = WHttpClientUtil.getPage(site, proxy);
        return html != null;
    }
}
