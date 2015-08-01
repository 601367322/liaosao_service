package com.xl.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xl.bean.Vip;

public class VipDao extends BaseDao {

	public Vip getVipByDeviceId(String deviceId) {
		List<Vip> list = (ArrayList<Vip>) getHibernateTemplate().find(
				"From Vip v where v.deviceId = '"
						+ deviceId
						+ "' and v.endTime > "
						+ new Date().getTime());
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
