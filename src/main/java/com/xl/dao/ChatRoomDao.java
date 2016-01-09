package com.xl.dao;

import com.xl.bean.ChatRoom;

import java.util.List;

/**
 * Created by Shen on 2016/1/3.
 */
public class ChatRoomDao extends BaseDao {

    public ChatRoom findByDeviceId(String deviceId) {
        String sql = "From ChatRoom where deviceId = '" + deviceId + "'";
        List list = getHibernateTemplate().find(sql);
        if (list != null && list.size() > 0) {
            return (ChatRoom) list.get(0);
        }
        return null;
    }
}
