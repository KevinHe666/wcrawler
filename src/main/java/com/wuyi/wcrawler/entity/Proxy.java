package com.wuyi.wcrawler.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by wuyi5 on 2017/8/25.
 */
public class Proxy implements Comparable<Proxy> {
    public static final int STORE_COLLECTOR = 0;
    public static final int STORE_CACHE = 1;
    public static final int STORE_CORE = 2;
    public static final int STORE_DB = 3;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String ip;
    private String port;

    public Proxy() {}

    public Proxy(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 代理成功率
     */
    private double successProbability;

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
    private long lastSuccessTimestamp;
    /**
     * 最近一次访问成功时间消耗
     * */
    private long lastSuccessTimeConsume;
    /**
     * 平均真实访问成功时间消耗
     * */
    private long avgSuccessTimeConsume;
    /**
     * 标记该proxy的存储状态:
     * 0: proxyCache存储过该proxy,proxyCore和database未存储过该proxy;
     * 1: proxyCore存储过该poxy;
     * 2: database存储过该proxy;
     * */
    private int storeStatus;

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

    public double getSuccessProbability() {
        return successProbability;
    }

    public void setSuccessProbability(double successProbability) {
        this.successProbability = successProbability;
    }

    public int getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(int successTimes) {
        this.successTimes = successTimes;
    }

    public int getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(int failureTimes) {
        this.failureTimes = failureTimes;
    }

    public long getLastSuccessTimestamp() {
        return lastSuccessTimestamp;
    }

    public void setLastSuccessTimestamp(long lastSuccessTimestamp) {
        this.lastSuccessTimestamp = lastSuccessTimestamp;
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

    public int getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(int storeStatus) {
        this.storeStatus = storeStatus;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", successProbability=" + successProbability +
                ", successTimes=" + successTimes +
                ", failureTimes=" + failureTimes +
                ", lastSuccessTimestamp=" + lastSuccessTimestamp +
                ", lastSuccessTimeConsume=" + lastSuccessTimeConsume +
                ", avgSuccessTimeConsume=" + avgSuccessTimeConsume +
                ", storeStatus=" + storeStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        if (id != proxy.id) return false;
        if (Double.compare(proxy.successProbability, successProbability) != 0) return false;
        if (successTimes != proxy.successTimes) return false;
        if (failureTimes != proxy.failureTimes) return false;
        if (lastSuccessTimestamp != proxy.lastSuccessTimestamp) return false;
        if (lastSuccessTimeConsume != proxy.lastSuccessTimeConsume) return false;
        if (avgSuccessTimeConsume != proxy.avgSuccessTimeConsume) return false;
        if (storeStatus != proxy.storeStatus) return false;
        if (ip != null ? !ip.equals(proxy.ip) : proxy.ip != null) return false;
        return port != null ? port.equals(proxy.port) : proxy.port == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        temp = Double.doubleToLongBits(successProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + successTimes;
        result = 31 * result + failureTimes;
        result = 31 * result + (int) (lastSuccessTimestamp ^ (lastSuccessTimestamp >>> 32));
        result = 31 * result + (int) (lastSuccessTimeConsume ^ (lastSuccessTimeConsume >>> 32));
        result = 31 * result + (int) (avgSuccessTimeConsume ^ (avgSuccessTimeConsume >>> 32));
        result = 31 * result + storeStatus;
        return result;
    }

    @Override
    public int compareTo(Proxy o) {
		if(successProbability > o.getSuccessProbability()) {
			return 1;
		}
		return 0;
	}
    
}
