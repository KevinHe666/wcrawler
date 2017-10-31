package com.wuyi.wcrawler.util;

import com.wuyi.wcrawler.Wcrawler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author wuyi5
 * @date 2017/8/28
 */
// 屏蔽里component, 肯定会影响其它的地方
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
//    @Autowired
    private static ApplicationContext ctx = null;
    private static  final String[] XML_PATH={"classpath:mybatis-druid.xml", "classpath:elastic-job-lite.xml", "spring.xml"};
    private static final Log LOG = LogFactory.getLog(ApplicationContextUtil.class);
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtil.ctx != null) {
            LOG.warn("ApplicationContext was override, the old one is " + ApplicationContextUtil.ctx);
        }
        ApplicationContextUtil.ctx = applicationContext;
    }

    public static ApplicationContext getCtx() {
        assertContextInjected();
        return ApplicationContextUtil.ctx;
    }


    private static void assertContextInjected() {
        if (ApplicationContextUtil.ctx == null) {
            LOG.warn("ApplicaitonContext is not initialized yet, initializing it now...");
            initApplicationContextFromClasspath(XML_PATH);
        }
    }

    private static void initApplicationContextFromClasspath(String[] xmlPath){
        ApplicationContextUtil.ctx = new ClassPathXmlApplicationContext(xmlPath);
    }

    public static <T> T getBean(String id) {
        assertContextInjected();
        return (T) ctx.getBean(id);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return ctx.getBean(requiredType);
    }

}
