package com.wuyi.wcrawler.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @authort wuyi5
 * @date 2017/10/25.
 */
public class ZhUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String urlToken;
    private String headline;
    private String businessName;
    private String educationsSchoolName;
    private String locationsName;
    private String linkPerson;
    private int followerCount;
    private int followingCount;
    private int voteupCount;
    private int thankedCount;
    private int answerCount;
    private int questionCount;
    private int articlesCount;
    /**
     * 0: 初始状态
     * 1: 正在使用
     * 2: 正常结束
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

    public String getUrlToken() {
        return urlToken;
    }

    public void setUrlToken(String urlToken) {
        this.urlToken = urlToken;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEducationsSchoolName() {
        return educationsSchoolName;
    }

    public void setEducationsSchoolName(String educationsSchoolName) {
        this.educationsSchoolName = educationsSchoolName;
    }

    public String getLocationsName() {
        return locationsName;
    }

    public void setLocationsName(String locationsName) {
        this.locationsName = locationsName;
    }

    public String getLinkPerson() {
        return linkPerson;
    }

    public void setLinkPerson(String linkPerson) {
        this.linkPerson = linkPerson;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getVoteupCount() {
        return voteupCount;
    }

    public void setVoteupCount(int voteupCount) {
        this.voteupCount = voteupCount;
    }

    public int getThankedCount() {
        return thankedCount;
    }

    public void setThankedCount(int thankedCount) {
        this.thankedCount = thankedCount;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(int articlesCount) {
        this.articlesCount = articlesCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ZhUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", urlToken='" + urlToken + '\'' +
                ", headline='" + headline + '\'' +
                ", businessName='" + businessName + '\'' +
                ", educationsSchoolName='" + educationsSchoolName + '\'' +
                ", locationsName='" + locationsName + '\'' +
                ", linkPerson='" + linkPerson + '\'' +
                ", followerCount=" + followerCount +
                ", followingCount=" + followingCount +
                ", voteupCount=" + voteupCount +
                ", thankedCount=" + thankedCount +
                ", answerCount=" + answerCount +
                ", questionCount=" + questionCount +
                ", articlesCount=" + articlesCount +
                ", status=" + status +
                '}';
    }
}
