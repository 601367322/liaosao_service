package com.xl.bean;

/**
 * UserTable entity. @author MyEclipse Persistence Tools
 */

public class UserTable implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deviceId;
	private String token;
	private String userName;
	private String phone;
	private String passWord;
	private Integer userType;
	private String otherToken;

	// Constructors

	/** default constructor */
	public UserTable() {
	}

	/** full constructor */
	public UserTable(String deviceId, String token, String userName,
			String phone, String passWord, Integer userType, String otherToken) {
		this.deviceId = deviceId;
		this.token = token;
		this.userName = userName;
		this.phone = phone;
		this.passWord = passWord;
		this.userType = userType;
		this.otherToken = otherToken;
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

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getOtherToken() {
		return this.otherToken;
	}

	public void setOtherToken(String otherToken) {
		this.otherToken = otherToken;
	}

}