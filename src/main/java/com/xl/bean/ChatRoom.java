package com.xl.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shen on 2016/1/3.
 */
public class ChatRoom implements Serializable{
    private int id;
    private String deviceId;
    private Double price;
    private Integer sex;
    private Integer minTime;
    private Integer maxTime;
    private Date createTime;

    private UserTable user;

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getMinTime() {
        return minTime;
    }

    public void setMinTime(Integer minTime) {
        this.minTime = minTime;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatRoom chatRoom = (ChatRoom) o;

        if (id != chatRoom.id) return false;
        if (deviceId != null ? !deviceId.equals(chatRoom.deviceId) : chatRoom.deviceId != null) return false;
        if (price != null ? !price.equals(chatRoom.price) : chatRoom.price != null) return false;
        if (sex != null ? !sex.equals(chatRoom.sex) : chatRoom.sex != null) return false;
        if (minTime != null ? !minTime.equals(chatRoom.minTime) : chatRoom.minTime != null) return false;
        if (maxTime != null ? !maxTime.equals(chatRoom.maxTime) : chatRoom.maxTime != null) return false;
        if (createTime != null ? !createTime.equals(chatRoom.createTime) : chatRoom.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (minTime != null ? minTime.hashCode() : 0);
        result = 31 * result + (maxTime != null ? maxTime.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
