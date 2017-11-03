package com.wuyi.wcrawler;

import com.wuyi.wcrawler.common.ProxySelectPolicy;
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
    private String [] xmlPaths = {"classpath:mybatis-druid.xml", "classpath:elastic-job-lite.xml", "spring.xml"};
    private long checkInterval = 100;
    private int tarAmount = 100;
    private long runningTime = 500 * 1000;
    private String startUserToken = "wu-yi-26-57";
    private boolean proxyFlag = true;
    private ProxySelectPolicy proxySelectPolicy = ProxySelectPolicy.RANDOM;
    private ZhUserMapper zhUserMapper = ApplicationContextUtil.getBean(ZhUserMapper.class);
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

    public static Config getInstance() {
        return instance == null ? newInstance() : instance;
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

        // 爬取的目标数量等于用户设置目标数量加上当前数据库中已爬取的数量
        this.tarAmount = tarAmount + zhUserMapper.selectCount(null);
        return this;
    }

    public Config setRunningTime(long runningTime) {
        this.runningTime = runningTime;
        return this;
    }

    public Config setProxySelectPolicy(ProxySelectPolicy proxySelectPolicy) {
        this.proxySelectPolicy = proxySelectPolicy;
        return this;
    }

    public Config setProxyFlag(boolean proxyFlag) {
        this.proxyFlag = proxyFlag;
        return this;
    }

    public boolean getProxyFlag() {
        return proxyFlag;
    }

    //    public Config setStartUserToken(String startUserToken) {
//        this.startUserToken = startUserToken;
//        ZhUser zhUser = new ZhUser();
//        zhUser.setStatus(0);
//        zhUser.setUrlToken(startUserToken);
//        zhUserMapper.insert(zhUser);
//        return this;
//    }

    public String[] getXmlPaths() {
        return xmlPaths;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public int getTarAmount() {
        return tarAmount;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public String getStartUserToken() {
        return startUserToken;
    }

    public ProxySelectPolicy getProxySelectPolicy() {
        return proxySelectPolicy;
    }
}
