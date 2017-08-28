package com.wuyi.wcrawler.bean;

/**
 * Created by wuyi5 on 2017/8/25.
 */
public class Proxy implements Comparable<Proxy>{
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
    /**
     * 标记该proxy的存储状态:
     * 0: proxyCache存储过该proxy,proxyQueue和database未存储过该proxy;
     * 1: proxyQueue存储过该poxy;
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

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
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
                ", quality=" + quality +
                ", successTimes=" + successTimes +
                ", failureTimes=" + failureTimes +
                ", lastSuccessTimeStamp=" + lastSuccessTimeStamp +
                ", lastSuccessTimeConsume=" + lastSuccessTimeConsume +
                ", avgSuccessTimeConsume=" + avgSuccessTimeConsume +
                ", storeStatus=" + storeStatus +
                '}';
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (avgSuccessTimeConsume ^ (avgSuccessTimeConsume >>> 32));
		result = prime * result + failureTimes;
		result = prime * result + id;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + (int) (lastSuccessTimeConsume ^ (lastSuccessTimeConsume >>> 32));
		result = prime * result + (int) (lastSuccessTimeStamp ^ (lastSuccessTimeStamp >>> 32));
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		long temp;
		temp = Double.doubleToLongBits(quality);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + storeStatus;
		result = prime * result + successTimes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proxy other = (Proxy) obj;
		if (avgSuccessTimeConsume != other.avgSuccessTimeConsume)
			return false;
		if (failureTimes != other.failureTimes)
			return false;
		if (id != other.id)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (lastSuccessTimeConsume != other.lastSuccessTimeConsume)
			return false;
		if (lastSuccessTimeStamp != other.lastSuccessTimeStamp)
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (Double.doubleToLongBits(quality) != Double.doubleToLongBits(other.quality))
			return false;
		if (storeStatus != other.storeStatus)
			return false;
		if (successTimes != other.successTimes)
			return false;
		return true;
	}

	public int compareTo(Proxy o) {
		// TODO Auto-generated method stub
		if(quality > o.getQuality()) {
			return 1;
		}
		return 0;
	}
    
}
