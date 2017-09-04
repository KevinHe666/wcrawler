package com.wuyi.wcrawler.proxy.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

/**
 * Created by wuyi5 on 2017/9/4.
 */
@Component(value = "jedis")
public class JedisUtil {
    private static Log LOG = LogFactory.getLog(JedisUtil.class);
    private static String IP;
    private static int PORT;
    private static Jedis jedis;

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
            jedis = new Jedis(IP, PORT);
            LOG.info("Redis connect: " + jedis.ping());
        } catch (Exception e) {
            LOG.error("Redis connect failed.");
        }
    }

    public static String get() {
        String mykey = jedis.get("myKey");
        return mykey;
    }

    public static void set() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient client = Redisson.create(config);
        RBucket<String> rBucket = client.getBucket("test");
        rBucket.set("123");
//        rBucket.set(2);
        LOG.info(rBucket.get());

    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml",
                "classpath:spring.xml");
//        LOG.info(JedisUtil.get());
        JedisUtil.set();
    }

}
