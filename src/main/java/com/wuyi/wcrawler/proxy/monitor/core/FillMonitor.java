package com.wuyi.wcrawler.proxy.monitor.core;

import java.util.concurrent.locks.Condition;

/**
 * Created by wuyi5 on 2017/8/31.
 */
public class FillMonitor extends CoreMonitor implements Runnable {
    private Condition condition;

    public FillMonitor(String monitorName, Condition condition) {
        super(monitorName);
        this.condition = condition;
    }

    public void run() {
        while (true) {
            while ()
        }
    }
}
