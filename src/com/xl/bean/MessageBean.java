package com.xl.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MessageBean")
public class MessageBean implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	private String msgId;
	private String userId;
	private String toId;
	private String fromId;
	private String content;
	private String time;
	private String nickName;
	private String userLogo;
	private Integer state;
	public MessageBean() {
		super();
		// TODO Auto-generated constructor stub
	}

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public MessageBean(String fromId, String toId, String content) {
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
    }

    public MessageBean(String msgId, String userId, String toId, String fromId,
			String content, String time, String nickName, String userLogo,
			int state) {
		super();
		this.msgId = msgId;
		this.userId = userId;
		this.toId = toId;
		this.fromId = fromId;
		this.content = content;
		this.time = time;
		this.nickName = nickName;
		this.userLogo = userLogo;
		this.state = state;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserLogo() {
		return userLogo;
	}
	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
