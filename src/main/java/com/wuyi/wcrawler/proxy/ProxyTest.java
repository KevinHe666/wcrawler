package com.wuyi.wcrawler.proxy;

import com.wuyi.wcrawler.entity.Proxy;
import com.wuyi.wcrawler.proxy.util.ProxyFilterUtil;
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
//    public static final String SITE = "https://www.zhihu.com/api/v4/members/wu-yi-26-57/followees?include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count,gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=0&limit=20er,following_count,question_count,voteup_count,thanked_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=0&limit=20";
    public static final String SITE = "https://www.zhihu.com";

    @Autowired
    ProxyCollector proxyCollector;
    @Autowired
    ProxyPool proxyPool;
    @Override
    public void run() {
        /** 循环检测下载的代理是否可用 */
        while(true) {
            Proxy proxy = proxyCollector.getProxy();
            if(testProxy(SITE, proxy)) {
                LOG.info(String.format("Test Success: ip %s port %s", proxy.getIp(), proxy.getPort()));
                /** 再次检测是否已经下载了该proxy  */
                if(!ProxyFilterUtil.contains(proxy)) {
                    ProxyFilterUtil.add(proxy);
                    proxyPool.getProxyCache().add(proxy);
                }
            } else {
                // TODO 一定要记得删除这行
                // 可以考虑先把代理下载下来，然后在使用过程中剔除不好的代理
//                proxyPool.getProxyCache().add(proxy);
                LOG.error(String.format("Test Failed: ip %s port %s", proxy.getIp(), proxy.getPort()));
            }
        }
    }

    public boolean testProxy(String site, Proxy proxy) {
        String html = WHttpClientUtil.getPage(site, proxy);
        return html != null;
    }
}
