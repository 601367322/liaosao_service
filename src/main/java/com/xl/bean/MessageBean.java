package com.xl.bean;

import java.io.Serializable;

public class MessageBean implements Serializable{

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
	private Integer msgType;
	public MessageBean() {
		super();
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

    public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
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

    public static final int TEXT = 0;
    public static final int VOICE = 1;
    public static final int IMAGE = 2;
    public static final int FACE = 3;
    public static final int RADIO = 4;

}
