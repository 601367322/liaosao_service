package com.xl.dao;

import com.xl.bean.Vip;
import com.xl.util.MyUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VipDao extends BaseDao<Vip> {

    @Cacheable(value = "Vip", key = "#deviceId")
    public Vip getVipByDeviceId(String deviceId) {
        List<Vip> list = (ArrayList<Vip>) getHibernateTemplate().find(
                "From Vip v where (v.deviceId = ? or v.deviceId = ?) and v.endTime > ?", deviceId, MyUtil.getmd5DeviceId(deviceId), new Date().getTime());
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Cacheable(value = "Vip", key = "#deviceId")
    public Vip getVipByDeviceIdAll(String deviceId) {
        List<Vip> list = (ArrayList<Vip>) getHibernateTemplate().find(
                "From Vip v where (v.deviceId = ? or v.deviceId = ?)", deviceId, MyUtil.getmd5DeviceId(deviceId));
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @CacheEvict(value = "Vip", key = "#obj.deviceId")
    @Override
    public Vip save(Vip obj) throws Exception {
        return super.save(obj);
    }

    @CacheEvict(value = "Vip", key = "#obj.deviceId")
    @Override
    public void saveOrUpdate(Vip obj) throws Exception {
        super.saveOrUpdate(obj);
    }

    @CacheEvict(value = "Vip", key = "#obj.deviceId")
    @Override
    public void update(Vip obj) throws Exception {
        super.update(obj);
    }
}
