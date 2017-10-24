package com.wuyi.wcrawler.bean;

/**
 *
 * @author wuyi5
 * @date 2017/8/29
 */
public class CrawlerTask {

    public static final int CREATED = 0;
    public static final int CRAWLING = 1;
    public static final int NORMALEND = 2;
    public static final int ABNORMALEND = 3;

    private int id;
    private String url;
    /**
     * 0: 初始状态
     * 1: 正在爬取
     * 2: 正常结束
     * 3: 异常终止
     * */
    private int status;
    /**
     * 当前爬取总数
     * */
    private int curAmount;
    /**
     * 目标爬取总数
     * */
    private int tarAmount;
    private long startTime;
    private long endedTime;
    private long spendTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(int curAmount) {
        this.curAmount = curAmount;
    }

    public int getTarAmount() {
        return tarAmount;
    }

    public void setTarAmount(int tarAmount) {
        this.tarAmount = tarAmount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndedTime() {
        return endedTime;
    }

    public void setEndedTime(long endedTime) {
        this.endedTime = endedTime;
    }

    public long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }

    @Override
    public String toString() {
        return "CrawlerTask{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", curAmount=" + curAmount +
                ", tarAmount=" + tarAmount +
                ", startTime=" + startTime +
                ", endedTime=" + endedTime +
                ", spendTime=" + spendTime +
                '}';
    }
}
