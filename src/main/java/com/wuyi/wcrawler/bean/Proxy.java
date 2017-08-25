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
     * 访问网页成功次数
     * */
    private int successTimes;
    /**
     * 真实访问网页失败次数
     * */
    private int failureTimes;
    /**
     * 最近一次访问成功时间
     * */
    private long lastSuccessTimeStamp;
    /**
     * 最近一次访问成功时间消耗
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

    public int getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(int realSuccessTimes) {
        this.successTimes = realSuccessTimes;
    }

    public int getFailureTimes() {
        return failureTimes;
    }

    public void seFailureTimes(int realFailureTimes) {
        this.failureTimes = realFailureTimes;
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
                ", successTimes=" + successTimes +
                ", failureTimes=" + failureTimes +
                ", lastSuccessTimeStamp=" + lastSuccessTimeStamp +
                ", lastSuccessTimeConsume=" + lastSuccessTimeConsume +
                ", avgSuccessTimeConsume=" + avgSuccessTimeConsume +
                '}';
    }
}
