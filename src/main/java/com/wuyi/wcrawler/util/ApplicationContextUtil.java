package com.wuyi.wcrawler.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by wuyi5 on 2017/8/28.
 */

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    @Autowired
    private ApplicationContext ctx;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    public Object getBean(String id) {
        return ctx.getBean(id);
    }

}
