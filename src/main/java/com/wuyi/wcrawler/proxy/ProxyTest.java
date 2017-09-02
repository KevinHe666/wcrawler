package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.util.WHttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/30.
 */
@Component(value = "proxyTest")
@Scope("prototype")
public class ProxyTest implements Runnable {
    private static Log LOG = LogFactory.getLog(ProxyTest.class);
    public static final String SITE = "https://www.zhihu.com/";
    @Autowired
    ProxyCollector proxyCollector;
    @Autowired
    ProxyPool proxyPool;
    public void run() {
        /** 循环检测下载的代理是否可用 */
        while(true) {
            Proxy proxy = proxyCollector.getProxy();
            if(testProxy(SITE, proxy)) {
                LOG.info(String.format("Test Success: ip %s port %s", proxy.getIp(), proxy.getPort()));
                proxyPool.getProxyCache().add(proxy);
            }
        }
    }

    public boolean testProxy(String site, Proxy proxy) {
        String html = WHttpClientUtil.getPage(site, proxy);
        return html != null;
    }
}
