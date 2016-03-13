package com.xl.dao;

import com.xl.bean.ChatRoom;
import com.xl.bean.UserBean;
import com.xl.bean.UserTable;
import com.xl.exception.MyException;
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
 * Created by Shen on 2016/1/3.
 */
public class ChatRoomDao extends BaseDao<ChatRoom> {

    private ChatRoomRequestDao chatRoomRequestDao;

    @Cacheable(value = "ChatRoom", key = "#deviceId")
    public ChatRoom findByDeviceId(String deviceId) {
        String sql = "From ChatRoom where deviceId = ? and state = 0";
        try {
            return (ChatRoom) getHibernateTemplate().find(sql, deviceId).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    public List<ChatRoom> findChatRoomByPage(int page) {
        final int number = 20;
        final int startId = page * number;
        final String sql = "select chatRoom,userTable From ChatRoom as chatRoom , UserTable as userTable where chatRoom.deviceId = userTable.deviceId and chatRoom.state = 0 order by chatRoom.id desc";
        List<ChatRoom> list = getHibernateTemplate().execute(new HibernateCallback<List<ChatRoom>>() {
            @Override
            public List<ChatRoom> doInHibernate(Session session) throws HibernateException {
                final Query query = session.createQuery(sql);
                query.setMaxResults(number);
                query.setFirstResult(startId);
                List<Object[]> list = query.list();
                List<ChatRoom> rooms = new ArrayList<ChatRoom>();
                for (int i = 0; i < list.size(); i++) {
                    ChatRoom room = (ChatRoom) list.get(i)[0];

                    //设置头像
                    UserTable ut = (UserTable) list.get(i)[1];
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

    public ChatRoom findChatRoomById(int id) throws MyException {
        String hql = "From ChatRoom where id = ? and state = 0";
        try {
            return (ChatRoom) getHibernateTemplate().find(hql, id).get(0);
        } catch (Exception e) {
            throw new MyException("已关闭");
        }
    }

    @CacheEvict(value = "ChatRoom", key = "#obj.deviceId")
    @Override
    public ChatRoom save(ChatRoom obj) throws Exception {
        return super.save(obj);
    }

    @CacheEvict(value = "ChatRoom", key = "#obj.deviceId")
    @Override
    public void saveOrUpdate(ChatRoom obj) throws Exception {
        super.saveOrUpdate(obj);
    }

    @CacheEvict(value = "ChatRoom", key = "#obj.deviceId")
    @Override
    public void update(ChatRoom obj) throws Exception {
        super.update(obj);
    }

    @CacheEvict(value = "ChatRoom", key = "#deviceId")
    public void deleteByDeviceId(String deviceId) throws Exception{
        ChatRoom room = findByDeviceId(deviceId);
        String hql = "Update ChatRoom set state = 2 where deviceId = ?";
        getHibernateTemplate().bulkUpdate(hql, deviceId);
        chatRoomRequestDao.deleteChatRoomRequestByRoom(deviceId, room);
    }

    public void setChatRoomRequestDao(ChatRoomRequestDao chatRoomRequestDao) {
        this.chatRoomRequestDao = chatRoomRequestDao;
    }
}
