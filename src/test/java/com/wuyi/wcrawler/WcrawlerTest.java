package com.wuyi.wcrawler;

import com.wuyi.wcrawler.util.WHttpClientUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Unit test for simple App.
 */
public class WcrawlerTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WcrawlerTest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( WcrawlerTest.class );
    }

    /**
     * Rigourous Test :-)
     */

    public void testProxySite() {
        CloseableHttpClient httpClient = WHttpClientUtil.getHttpClient();
        String xicidailiUrl = "http://www.xicidaili.com"; // not open
        String miniipUrl = "http://www.mimiip.com/gnpu/"; // ok
        String ip181Url = "http://www.ip181.com"; // not open

        String page = WHttpClientUtil.getPage(httpClient, ip181Url, false);
        System.out.println(page);
        assertTrue( true );
    }

    public void testName() {
        assertEquals('A', 'A');
    }
}
