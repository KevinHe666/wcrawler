package com.wuyi.wcrawler.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @authort wuyi
 * @date 2017/11/9.
 */
public class FollowRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String follower;
    private String followee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }

    @Override
    public String toString() {
        return "FollowRelation{" +
                "id=" + id +
                ", follower='" + follower + '\'' +
                ", followee='" + followee + '\'' +
                '}';
    }
}
