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
                return list.get(0);
            }
        }
        return null;
    }
    @CacheEvict(value = "UserTable",key = "#obj.deviceId")
    @Override
    public UserTable save(UserTable obj) throws Exception {
        UserTable table = null;
        try {
            table = super.save(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
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
