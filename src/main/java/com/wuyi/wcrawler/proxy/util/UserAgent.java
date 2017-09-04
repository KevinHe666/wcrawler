package com.wuyi.wcrawler.proxy.util;

import java.util.Random;

/**
 * Created by wuyi5 on 2017/8/28.
 */
public enum UserAgent {
    UA0("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)"),
    IE1("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2)"),
    IE2("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"),
    IE3("Mozilla/4.0 (compatible; MSIE 5.0; Windows NT"),
    FIREFOX0("Mozilla/5.0 (Windows; U; Windows NT 5.2) Gecko/2008070208 Firefox/3.0.1"),
    FIREFOX1("Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070309 Firefox/2.0.0.3"),
    FIREFOX2("Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070803 Firefox/1.5.0.12"),
    OPERA0("Opera/9.27 (Windows NT 5.2; U; zh-cn)"),
    SOUGOU0("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0)"),
    SOUGOU1("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.33 Safari/534.3 SE 2.X MetaSr 1.0"),
    Three360("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)"),
    OPERA1("Opera/8.0 (Macintosh; PPC Mac OS X; U; en)"),
    QQ0("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1 QQBrowser/6.9.11079.201"),
    QQ1("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E) QQBrowser/6.9.11079.201"),
    AYU0("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"),
    CHROME0("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3088.3 Safari/537.36"),
    SAFARI0("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/602.4.8 (KHTML, like Gecko) Version/10.0.3 Safari/602.4.8"),
    SAFARI1("Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1 Safari/525.13");
    private String ua;

    UserAgent(String ua) {
        this.ua = ua;
    }

    public static String getUA() {
       Random random = new Random();
       return UserAgent.values()[random.nextInt(UserAgent.values().length)].ua;
    }
}
