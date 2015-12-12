package com.xl.bean;

import net.sf.json.JSONObject;

public class UserUtil {

	public static UserBean getUserBeanByUserTable(UserTable ut){
		return (UserBean) JSONObject.toBean(JSONObject.fromObject(ut.getDetail()),UserBean.class);
	}
}
