package com.xl.dao;

import com.xl.bean.UserTable;

import java.util.List;

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

}
