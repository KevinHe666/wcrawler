package com.wuyi.wcrawler;

import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.service.impl.CrawlerUrlServiceImpl;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author wuyi5
 */
public class Wcrawler {
        private static final Log LOG = LogFactory.getLog(Wcrawler.class);
        public static void main(String[] args ) throws InterruptedException {
        //classpath后面冒号不能有空格, 不然会FileNotFoundException
        new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml", "classpath:elastic-job-lite.xml",
                "spring.xml");
        ApplicationContextUtil acu = new ApplicationContextUtil();
        ZhUserMapper zhUserMapper = acu.getBean(ZhUserMapper.class);
        while (true) {
            TimeUnit.MILLISECONDS.sleep(100);
            List<ZhUser> zhUserList = zhUserMapper.select(null);
            if (zhUserList.size() > 5) {
                LOG.info("Wcrawler scrap total user: " + zhUserList.size());
                LOG.info("Wcrawler ended...");
                System.exit(0);
            }
        }
    }
}
