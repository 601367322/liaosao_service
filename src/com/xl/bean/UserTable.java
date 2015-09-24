package com.xl.bean;

import com.xl.util.DefaultDefaultValueProcessor;
import net.sf.json.JSONObject;

/**
 * UserTable entity. @author MyEclipse Persistence Tools
 */

public class UserTable implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deviceId;
	private String detail;

	// Constructors

	/** default constructor */
	public UserTable() {
	}

	/** minimal constructor */
	public UserTable(String deviceId) {
		this.deviceId = deviceId;
	}

	/** full constructor */
	public UserTable(String deviceId, String detail) {
		this.deviceId = deviceId;
		this.detail = detail;
	}

	// Property accessors

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
        return (UserBean) JSONObject.toBean(
                JSONObject.fromObject(detail), UserBean.class);
    }

    public void setUserBean(UserBean userBean){
        setDetail(JSONObject.fromObject(userBean,
                DefaultDefaultValueProcessor.getJsonConfig()).toString());
    }

	public void setDetail(String detail) {
		this.detail = detail;
	}

}