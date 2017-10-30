package com.wuyi.wcrawler;

import com.wuyi.wcrawler.entity.ZhUser;
import com.wuyi.wcrawler.mapper.ZhUserMapper;
import com.wuyi.wcrawler.util.ApplicationContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;

/**
 * @authort wuyi
 * @date 2017/10/26.
 */
public class Config {
    private static final Log LOG = LogFactory.getLog(Config.class);
    public String [] xmlPaths = {"classpath:mybatis-druid.xml", "classpath:elastic-job-lite.xml", "spring.xml"};
    public long checkInterval = 100;
    public int tarAmount = 500;
    public long runningTime = 300 * 1000;
    private static Config instance;
    public static Config newInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public Config setXMLPaths(String[] newXMLPaths) {
        xmlPaths = null;
        xmlPaths = newXMLPaths;
        return this;
    }

    public Config addXMLPath(String xmlPath) {
        String[] oldXMLPaths = xmlPaths;
        String[] newXMLPaths = new String[oldXMLPaths.length + 1];
        System.arraycopy(oldXMLPaths, 0, newXMLPaths, 0, oldXMLPaths.length);
        newXMLPaths[oldXMLPaths.length] = xmlPath;
        xmlPaths = newXMLPaths;
        oldXMLPaths = null;
        newXMLPaths = null;
        return this;
    }

    public Config setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
        return this;
    }

    public Config setTarAmount(int tarAmount) {
        ZhUserMapper zhUserMapper = ApplicationContextUtil.getBean(ZhUserMapper.class);
        // 爬取的目标数量等于用户设置目标数量加上当前数据库中已爬取的数量
        this.tarAmount = tarAmount + zhUserMapper.selectCount(null);
        return this;
    }

    public Config setRunningTime(long runningTime) {
        this.runningTime = runningTime;
        return this;
    }

}
