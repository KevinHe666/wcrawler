package com.wuyi.wcrawler.proxy.monitor;

import com.wuyi.wcrawler.bean.Proxy;
import com.wuyi.wcrawler.proxy.ProxyPool;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wuyi5 on 2017/8/30.
 */
public class Monitor {
    private long startTime;
    private long endedTime;
    private long runningTime;
    private String monitorName;

    @Autowired
    public ProxyPool proxyPool;

    public Monitor(String monitorName) {
        this.monitorName = monitorName;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getStartTime() {
        return startTime;
    }

    public long getEndedTime() {
        return endedTime;
    }

    public void setEndedTime(long endedTime) {
        this.endedTime = endedTime;
    }

    public long getRunningTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }
}
