package com.wuyi.wcrawler.bean;

/**
 * Created by wuyi5 on 2017/8/25.
 */
public class Proxy {
    private int id;
    private String ip;
    private String port;

    /**
     * 代理的质量评分
     * */
    private double quality;
    /**
     * 真实访问网页成功次数
     * */
    private int realSuccessTimes;
    /**
     * 测试访问网页成功次数
     * */
    private int testSuccessTimes;
    /**
     * 真实访问网页失败次数
     * */
    private int realFailureTimes;
    /**
     * 测试访问网页失败次数
     * */
    private int testFailureTimes;
    /**
     * 最近一次真实访问成功时间
     * */
    private long lastSuccessTimeStamp;
    /**
     * 最近一次真实访问成功时间消耗
     * */
    private long lastSuccessTimeConsume;
    /**
     * 平均真实访问成功时间消耗
     * */
    private long avgSuccessTimeConsume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public int getRealSuccessTimes() {
        return realSuccessTimes;
    }

    public void setRealSuccessTimes(int realSuccessTimes) {
        this.realSuccessTimes = realSuccessTimes;
    }

    public int getTestSuccessTimes() {
        return testSuccessTimes;
    }

    public void setTestSuccessTimes(int testSuccessTimes) {
        this.testSuccessTimes = testSuccessTimes;
    }

    public int getRealFailureTimes() {
        return realFailureTimes;
    }

    public void setRealFailureTimes(int realFailureTimes) {
        this.realFailureTimes = realFailureTimes;
    }

    public int getTestFailureTimes() {
        return testFailureTimes;
    }

    public void setTestFailureTimes(int testFailureTimes) {
        this.testFailureTimes = testFailureTimes;
    }

    public long getLastSuccessTimeStamp() {
        return lastSuccessTimeStamp;
    }

    public void setLastSuccessTimeStamp(long lastSuccessTimeStamp) {
        this.lastSuccessTimeStamp = lastSuccessTimeStamp;
    }

    public long getLastSuccessTimeConsume() {
        return lastSuccessTimeConsume;
    }

    public void setLastSuccessTimeConsume(long lastSuccessTimeConsume) {
        this.lastSuccessTimeConsume = lastSuccessTimeConsume;
    }

    public long getAvgSuccessTimeConsume() {
        return avgSuccessTimeConsume;
    }

    public void setAvgSuccessTimeConsume(long avgSuccessTimeConsume) {
        this.avgSuccessTimeConsume = avgSuccessTimeConsume;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", quality=" + quality +
                ", realSuccessTimes=" + realSuccessTimes +
                ", testSuccessTimes=" + testSuccessTimes +
                ", realFailureTimes=" + realFailureTimes +
                ", testFailureTimes=" + testFailureTimes +
                ", lastSuccessTimeStamp=" + lastSuccessTimeStamp +
                ", lastSuccessTimeConsume=" + lastSuccessTimeConsume +
                ", avgSuccessTimeConsume=" + avgSuccessTimeConsume +
                '}';
    }
}
