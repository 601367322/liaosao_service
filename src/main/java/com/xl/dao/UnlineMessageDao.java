package com.xl.dao;

import java.util.ArrayList;
import java.util.List;

import com.xl.bean.UnlineMessage;
import com.xl.bean.UserTable;

public class UnlineMessageDao extends BaseDao {

	public List<UnlineMessage> getMyUnlineMessage(String deviceId) {
		List<UnlineMessage> list = new ArrayList<UnlineMessage>();
		list = (List<UnlineMessage>) getHibernateTemplate().find(
				"From UnlineMessage u where u.toId='" + deviceId + "'");
		return list;
	}

	public void deleteAll(List list) {
		getHibernateTemplate().deleteAll(list);
	}

}
