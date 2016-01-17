package com.xl.dao;

import com.xl.bean.UserTable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public class UserDao extends BaseDao<UserTable> {

    @Cacheable(value = "UserTable", key = "#deviceId")
    public UserTable getUserByDeviceId(String deviceId) {
        if (deviceId != null) {
            List<UserTable> list = (List<UserTable>) getHibernateTemplate()
                    .find("From UserTable u where u.deviceId='" + deviceId
                            + "'");
            if (list != null && list.size() > 0) {
                UserTable ut = list.get(0);
                currentSession().evict(ut);
                return ut;
            }
        }
        return null;
    }
    @CacheEvict(value = "UserTable",key = "#obj.deviceId")
    @Override
    public Object save(UserTable obj) throws Exception {
        return super.save(obj);
    }
    @CacheEvict(value = "UserTable",key = "#obj.deviceId")
    @Override
    public void saveOrUpdate(UserTable obj) throws Exception {
        super.saveOrUpdate(obj);
    }
    @CacheEvict(value = "UserTable",key = "#obj.deviceId")
    @Override
    public void update(UserTable obj) throws Exception {
        super.update(obj);
    }
}