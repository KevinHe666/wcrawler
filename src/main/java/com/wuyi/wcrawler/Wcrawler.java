package com.wuyi.wcrawler;

import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import com.wuyi.wcrawler.util.WcrawlerStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.*;

/**
 *
 * @author wuyi5
 */
public class Wcrawler implements Runnable{
    private static final Log LOG = LogFactory.getLog(Wcrawler.class);
    private Config config;
    private long startTime;
    private WcrawlerStatus status = WcrawlerStatus.INIT;
    private ExecutorService executorService;
    public Wcrawler() {
        this(new Config(), Executors.newSingleThreadExecutor());
    }
    public Wcrawler(Config config, ExecutorService executorService) {
        LOG.info("Wcrawler INIT !");
        this.config = config;
        this.executorService = executorService;
    }
    public void start() {
        LOG.info("Wcrawler START !");
        // 加载配置文件, 默认启动job
        //classpath后面冒号不能有空格, 不然会FileNotFoundException
        new ClassPathXmlApplicationContext(config.xmlPaths);
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    public Config getConfig() {
        return config;
    }
    @Override
    public void run() {
        start();
        ZhUserMapper zhUserMapper = ApplicationContextUtil.getBean(ZhUserMapper.class);
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(config.checkInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<ZhUser> zhUserList = zhUserMapper.select(null);
            if (zhUserList.size() > config.tarAmount) {
                LOG.info("Wcrawler scrap total user: " + zhUserList.size());
                zhUserMapper = null;
                status = WcrawlerStatus.STOPPING;
                break;
            }
            if (System.currentTimeMillis() - startTime > config.runningTime) {
                LOG.info("Wcrawler's running time exceed...");
                status = WcrawlerStatus.STOPPING;
                break;
            }
        }
        if (status == WcrawlerStatus.STOPPING) {
            LOG.info("Wcrawler STOPPING...");
            executorService.shutdownNow();
        }
        if (!executorService.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (executorService.isTerminated()) {
            LOG.info("Wcrawler STOP.");
        }
    }
    public static void main(String[] args ) throws InterruptedException {
        Config config = Config.newInstance();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Wcrawler wcrawler = new Wcrawler(config, executorService);
        executorService.execute(wcrawler);
    }
}
