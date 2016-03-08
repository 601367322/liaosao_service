package com.xl.dao;

import com.xl.bean.ChatRoom;
import com.xl.bean.ChatRoomRequest;
import com.xl.bean.UserBean;
import com.xl.bean.UserTable;
import com.xl.util.MyRequestUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.hibernate4.HibernateCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2016/1/23.
 */
public class ChatRoomRequestDao extends BaseDao<ChatRoomRequest> {


    @Cacheable(value = "ChatRoomRequest", key = "#deviceId")
    public List<ChatRoomRequest> findRequestListByDeviceId(String deviceId,int page) {

        final int number = 20;
        final int startId = page * number;
        final String sql = "select chatRommRequest,chatRoom,userTable From ChatRoomRequest as chatRommRequest, ChatRoom as chatRoom , UserTable as userTable where chatRoom.deviceId = userTable.deviceId and chatRoom.state = 0 and chatRoom.id = chatRommRequest.roomId order by chatRommRequest.id desc";
        List<ChatRoomRequest> list = getHibernateTemplate().execute(new HibernateCallback<List<ChatRoomRequest>>() {
            @Override
            public List<ChatRoomRequest> doInHibernate(Session session) throws HibernateException {
                final Query query = session.createQuery(sql);
                query.setMaxResults(number);
                query.setFirstResult(startId);
                List<Object[]> list = query.list();
                List<ChatRoomRequest> rooms = new ArrayList<ChatRoomRequest>();
                for (int i = 0; i < list.size(); i++) {
                    ChatRoomRequest room = (ChatRoomRequest) list.get(i)[0];

                    room.setChatRoom((ChatRoom) list.get(i)[1]);

                    //设置头像
                    UserTable ut = (UserTable) list.get(i)[2];
                    UserBean userBean = ut.getUserBean();
                    userBean.setLogo(MyRequestUtil.getHost(null) + userBean.getLogo());
                    ut.setUserBean(userBean);

                    room.setUser(ut);
                    rooms.add(room);
                }
                return rooms;
            }
        });
        return list;
    }

    @CacheEvict(value = "ChatRoomRequest", key = "#deviceId")
    public void deleteChatRoomRequestByRoomId(String deviceId, int roomId) {
        String hql = "update ChatRoomRequest set state = 3 where roomId = ?";
        getHibernateTemplate().bulkUpdate(hql, roomId);
    }

    

}
