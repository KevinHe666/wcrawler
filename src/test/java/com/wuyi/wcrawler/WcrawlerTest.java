package com.wuyi.wcrawler;

import com.wuyi.wcrawler.common.ProxySelectPolicy;
import com.wuyi.wcrawler.util.WHttpClientUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * test for Wcrawler
 */
public class WcrawlerTest {
    public static void main(String[] args ) throws InterruptedException {
        Config config = Config.newInstance()
                .setTarAmount(50)
                .setRunningTime(3600 * 1000)
                .setCheckInterval(500)
                .setProxyFlag(true)
                .setProxySelectPolicy(ProxySelectPolicy.RANDOM);
        Wcrawler wcrawler = new Wcrawler(config);
        wcrawler.start();
    }
}
