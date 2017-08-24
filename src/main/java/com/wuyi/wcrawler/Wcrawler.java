package com.wuyi.wcrawler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class Wcrawler {
    public static void main( String[] args ) {
        /**classpath后面冒号不能有空格, 不然会FileNotFoundException*/
        new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml", "classpath:elastic-job-lite.xml",
                "spring.xml");
    }
}
