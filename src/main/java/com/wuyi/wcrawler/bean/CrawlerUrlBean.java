package com.wuyi.wcrawler.bean;

/**
 * Created by wuyi5 on 2017/8/16.
 */
public class CrawlerUrlBean {
    private int id;
    private String name;
    private String url;
    private int parent;
    /**
     * 0: 初始状态
     * 1: 正在爬取
     * 2: 爬取正常结束
     * 3: 爬取异常结束
     * */
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CrawlerUrl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", parent=" + parent +
                ", status=" + status +
                '}';
    }
}
