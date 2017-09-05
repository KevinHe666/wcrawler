package com.wuyi.wcrawler.proxy.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import sun.security.provider.MD5;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;

/**
 * Created by wuyi5 on 2017/9/4.
 */
@Component(value = "jedis")
public class RedissonUtil {
    private static Log LOG = LogFactory.getLog(RedissonUtil.class);
    private static String IP;
    private static int PORT;

    @Value("${redis.server.ip}")
    public void setIp(String ip) {
        IP = ip;
    }

    @Value("${redis.server.port}")
    public void setPort(int port) {
        PORT = port;
    }

    @PostConstruct
    public static void connect() {
        try {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://127.0.0.1:6379");
            RedissonClient client = Redisson.create(config);
            LOG.info("Redis connect: " + client);
        } catch (Exception e) {
            LOG.error("Redis connect failed.");
        }
    }


    public static void set() {
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml",
                "classpath:spring.xml");

        RedissonUtil.set();
    }

}
