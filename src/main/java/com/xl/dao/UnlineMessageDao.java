package com.xl.dao;

import com.xl.bean.UnlineMessage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

public class UnlineMessageDao extends BaseDao {

    @Cacheable(value = "UnlineMessage", key = "#deviceId")
    public List<UnlineMessage> getMyUnlineMessage(String deviceId) {
        List<UnlineMessage> list = new ArrayList<UnlineMessage>();
        list = (List<UnlineMessage>) getHibernateTemplate().find(
                "From UnlineMessage u where u.toId='" + deviceId + "'");
        return list;
    }

    @CacheEvict(value = "UnlineMessage", key = "#deviceId")
    public void deleteAll(String deviceId, List list) {
        getHibernateTemplate().deleteAll(list);
    }

}
