package com.xl.dao;

import java.util.List;

import com.xl.bean.UserTable;

public class UserDao extends BaseDao {

	public UserTable getUserByDeviceId(String deviceId) {
		if (deviceId != null) {
			List<UserTable> list = (List<UserTable>) getHibernateTemplate()
					.find("From UserTable u where u.deviceId='" + deviceId
							+ "'");
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	public UserTable getUserByToken(String token) {
		if (token != null) {
			List<UserTable> list = (List<UserTable>) getHibernateTemplate()
					.find("From UserTable u where u.token='" + token + "'");
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
}
