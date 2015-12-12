package com.xl.bean;

/**
 * Vip entity. @author MyEclipse Persistence Tools
 */

public class Vip implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deviceId;
	private Long createTime;
	private Long endTime;

	// Constructors

	/** default constructor */
	public Vip() {
	}

	/** full constructor */
	public Vip(String deviceId, Long createTime, Long endTime) {
		this.deviceId = deviceId;
		this.createTime = createTime;
		this.endTime = endTime;
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

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

}