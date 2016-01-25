package com.xl.bean;

import java.util.Date;

/**
 * Created by Shen on 2016/1/23.
 */
public class ChatRoomRequest {
    private int id;
    private int roomId;
    private int times;
    private String deviceIdForRequester;
    private String deviceIdForMaster;
    private Date createTime;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getDeviceIdForRequester() {
        return deviceIdForRequester;
    }

    public void setDeviceIdForRequester(String deviceIdForRequester) {
        this.deviceIdForRequester = deviceIdForRequester;
    }

    public String getDeviceIdForMaster() {
        return deviceIdForMaster;
    }

    public void setDeviceIdForMaster(String deviceIdForMaster) {
        this.deviceIdForMaster = deviceIdForMaster;
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

        ChatRoomRequest that = (ChatRoomRequest) o;

        if (id != that.id) return false;
        if (roomId != that.roomId) return false;
        if (times != that.times) return false;
        if (deviceIdForRequester != null ? !deviceIdForRequester.equals(that.deviceIdForRequester) : that.deviceIdForRequester != null)
            return false;
        if (deviceIdForMaster != null ? !deviceIdForMaster.equals(that.deviceIdForMaster) : that.deviceIdForMaster != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + roomId;
        result = 31 * result + times;
        result = 31 * result + (deviceIdForRequester != null ? deviceIdForRequester.hashCode() : 0);
        result = 31 * result + (deviceIdForMaster != null ? deviceIdForMaster.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
