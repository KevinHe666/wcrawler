package com.wuyi.wcrawler.proxy.monitor;

import com.wuyi.wcrawler.proxy.ProxyPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/30.
 */
@Component(value = "monitor")
public class Monitor {
    @Autowired
    public ProxyPool proxyPool;

    private long startTime;
    private long endedTime;
    private long runningTime;

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

}
