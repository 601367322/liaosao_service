package com.xl.dao;

import com.xl.bean.Account;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by Shen on 2015/12/20.
 */
public class AccountDao extends BaseDao<Account> {

    @Cacheable(value = "Account",key = "#deviceId")
    public Account getAccountByDeviceId(String deviceId) {
        List<Account> list = (List<Account>) getHibernateTemplate().find("From Account where deviceId = '" + deviceId + "'");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @CacheEvict(value = "Account",key = "#obj.deviceId")
    @Override
    public void saveOrUpdate(Account obj) throws Exception {
        super.saveOrUpdate(obj);
    }

    @CacheEvict(value = "Account",key = "#obj.deviceId")
    @Override
    public Object save(Account obj) throws Exception {
        return super.save(obj);
    }

    @CacheEvict(value = "Account",key = "#obj.deviceId")
    @Override
    public void update(Account obj) throws Exception {
        super.update(obj);
    }
}
