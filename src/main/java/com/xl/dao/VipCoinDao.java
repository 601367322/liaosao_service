package com.xl.dao;

import com.xl.bean.VipCoin;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by Shen on 2015/10/11.
 */
public class VipCoinDao extends BaseDao<VipCoin> {

    @Cacheable(value = "VipCoin", key = "#id")
    public VipCoin getCoinById(int id) {
        return getHibernateTemplate().get(VipCoin.class, id);
    }

    @CacheEvict(value = "VipCoin", key = "#obj.id")
    @Override
    public Object save(VipCoin obj) throws Exception {
        return super.save(obj);
    }

    @CacheEvict(value = "VipCoin", key = "#obj.id")
    @Override
    public void saveOrUpdate(VipCoin obj) throws Exception {
        super.saveOrUpdate(obj);
    }

    @CacheEvict(value = "VipCoin", key = "#obj.id")
    @Override
    public void update(VipCoin obj) throws Exception {
        super.update(obj);
    }
}
