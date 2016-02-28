package com.xl.dao;

import com.xl.bean.Pay;
import com.xl.util.MyUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by Shen on 2015/10/10.
 */
public class PayDao extends BaseDao<Pay> {

    @Cacheable(value = "Pay", key = "#orderId")
    public Pay getPay(String orderId) {
        return getHibernateTemplate().get(Pay.class, orderId);
    }

    @CacheEvict(value = "Pay", key = "#obj.out_trade_no")
    @Override
    public Object save(Pay obj) throws Exception {
        return super.save(obj);
    }

    @CacheEvict(value = "Pay", key = "#obj.out_trade_no")
    @Override
    public void saveOrUpdate(Pay obj) throws Exception {
        super.saveOrUpdate(obj);
    }

    @CacheEvict(value = "Pay", key = "#obj.out_trade_no")
    @Override
    public void update(Pay obj) throws Exception {
        super.update(obj);
    }

    public List<Pay> getPayListByDeviceId(String deviceId) {
        return (List<Pay>) getHibernateTemplate().find("From Pay where body = ? order by create_time desc", MyUtil.getmd5DeviceId(deviceId));
    }
}
