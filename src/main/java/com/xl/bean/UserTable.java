package com.xl.bean;

import com.xl.util.MyJSONUtil;

import java.io.Serializable;

public class UserTable implements Serializable {

	private Integer id;
	private String deviceId;
	private String detail;

	public UserTable() {
	}

	public UserTable(String deviceId) {
		this.deviceId = deviceId;
	}

	public UserTable(String deviceId, String detail) {
		this.deviceId = deviceId;
		this.detail = detail;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDetail() {
		return this.detail;
	}

    public UserBean getUserBean(){
        return MyJSONUtil.jsonToBean(detail,UserBean.class);
    }

    public void setUserBean(UserBean userBean){
        setDetail(MyJSONUtil.beanToJson(userBean).toString());
    }

	public void setDetail(String detail) {
		this.detail = detail;
	}

}