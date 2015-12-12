package com.xl.dao;

import com.xl.bean.Vip;
import com.xl.util.MyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VipDao extends BaseDao {

    public Vip getVipByDeviceId(String deviceId) {
        deviceId = MyUtil.getmd5DeviceId(deviceId);
        List<Vip> list = (ArrayList<Vip>) getHibernateTemplate().find(
                "From Vip v where v.deviceId = '" + deviceId
                        + "' and v.endTime > " + new Date().getTime());
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public Vip getVipByDeviceIdAll(String deviceId) {
        deviceId = MyUtil.getmd5DeviceId(deviceId);
        List<Vip> list = (ArrayList<Vip>) getHibernateTemplate().find(
                "From Vip v where v.deviceId = '" + deviceId + "'");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
