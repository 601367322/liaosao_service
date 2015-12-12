package com.xl.bean;

/**
 * UnlineMessage entity. @author MyEclipse Persistence Tools
 */

public class UnlineMessage implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fromId;
	private String toId;
	private String message;

	// Constructors

	/** default constructor */
	public UnlineMessage() {
	}

	/** full constructor */
	public UnlineMessage(String fromId, String toId, String message) {
		this.fromId = fromId;
		this.toId = toId;
		this.message = message;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFromId() {
		return this.fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return this.toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}