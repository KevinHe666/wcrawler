package com.wuyi.wcrawler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

    public void testApp() {
        assertTrue( true );
    }

    public void testName() {
        assertEquals('A', 'A');
    }
}
