package com.wuyi.wcrawler.proxy.test;

/**
 * Created by wuyi5 on 2017/8/30.
 */
public enum  ProxyTestSite {
    SITE0("https://www.zhihu.com/");
    String SITE;
    ProxyTestSite(String site) {
        this.SITE = site;
    }

    public String getSite() {
        return this.SITE;
    }
}
