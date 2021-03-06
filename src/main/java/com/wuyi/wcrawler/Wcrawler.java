package com.wuyi.wcrawler;

import com.wuyi.wcrawler.common.WcrawlerStatus;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.*;

/**
 *
 * @author wuyi5
 */
public class Wcrawler implements Runnable{
    private static final Log LOG = LogFactory.getLog(Wcrawler.class);
    private Config config;
    private ZhUserMapper zhUserMapper;
    private long startTime;
    private long preTotalUsers;
    private long curTotalUsers;
    private WcrawlerStatus status = WcrawlerStatus.INIT;
    private ExecutorService executorService;
    public Wcrawler() {
        this(new Config());
    }
    public Wcrawler(Config config) {
        LOG.info("Wcrawler INIT !");
        this.config = config;
        startTime = System.currentTimeMillis();
        zhUserMapper = ApplicationContextUtil.getBean(ZhUserMapper.class);
        preTotalUsers = zhUserMapper.selectCount(null);
        curTotalUsers = preTotalUsers;
        this.executorService = Executors.newSingleThreadExecutor();
    }
    public void start() {
        LOG.info("Wcrawler START !");
        // 加载配置文件, 默认启动job
        //classpath后面冒号不能有空格, 不然会FileNotFoundException
        // 这里重复加载spring的配置的文件了(上面getBean时已经加载过一遍)
//        new ClassPathXmlApplicationContext(config.xmlPaths);

        executorService.execute(this);
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    public Config getConfig() {
        return config;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(config.getCheckInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            curTotalUsers = zhUserMapper.selectCount(null);
            if (curTotalUsers > config.getTarAmount()) {
                LOG.info("Yet, Wcrawler scrap total user: " + (curTotalUsers - preTotalUsers));
                zhUserMapper = null;
                status = WcrawlerStatus.STOPPING;
                break;
            }
            if (System.currentTimeMillis() - startTime > config.getRunningTime()) {
                LOG.info("Wcrawler's running time exceed...");
                status = WcrawlerStatus.STOPPING;
                break;
            }
        }
        if (status == WcrawlerStatus.STOPPING) {
            LOG.info("Wcrawler STOPPING...");
            executorService.shutdownNow();
            LOG.info("Wcrawler STOP.");
            System.exit(-1);
        }
    }
}
