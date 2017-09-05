package com.wuyi.wcrawler.proxy.util;

import com.wuyi.wcrawler.bean.Proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.Redisson;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wuyi5 on 2017/9/4.
 */
@Component(value = "proxyFilter")
public class ProxyFilterUtil {
    private static Log LOG = LogFactory.getLog(ProxyFilterUtil.class);
    private static String IP;
    private static int PORT;
    private static RedissonClient client;
    private static RReadWriteLock rwLock;
    private static RSet<byte[]> rSet;

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
            config.useSingleServer().setAddress("redis://" + IP + ":" + PORT);
            /** 注意：这里连接完成后，会一直保持连接不断开，可以用shutdown()断开 */
            client = Redisson.create(config);
            LOG.info("Redis connect: " + client);
        } catch (Exception e) {
            LOG.error("Redis connect failed.");
        }
    }

    @PreDestroy
    public static void destroy() {
        client.shutdown();
    }

    public static boolean contains(Proxy proxy) {
        rwLock = client.getReadWriteLock("ProxyFilterLock");
        rwLock.readLock().lock();
        try {
            byte[] hashProxy = hashByMD5(proxy);
            rSet = client.getSet("ProxyFilterSet");
            if (rSet.contains(hashProxy)) {
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            rwLock.readLock().unlock();
        }
        return false;
    }

    public static void add(Proxy proxy) {
        rwLock = client.getReadWriteLock("ProxyFilterLock");
        rwLock.writeLock().lock();
        try {
            byte[] hashProxy = hashByMD5(proxy);
            rSet = client.getSet("ProxyFilterSet");
            rSet.add(hashProxy);
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public static byte[] hashByMD5(Proxy proxy) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String simpleProxy = proxy.getIp() + proxy.getPort();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] hashProxy = md5.digest(simpleProxy.getBytes("utf-8"));
        return hashProxy;
    }

//    public static void main(String[] args) {
//        new ClassPathXmlApplicationContext("classpath:mybatis-druid.xml",
//                "spring.xml");
//        if(ProxyFilterUtil.contains(new Proxy("58.22.61.211", "3128"))) {
//            LOG.info("true");
//        } else {
//            LOG.info("false");
//        }
//        if(ProxyFilterUtil.contains(new Proxy("58.22.61.212", "3128"))) {
//            LOG.info("true");
//        } else {
//            LOG.info("false");
//        }
//    }

}
