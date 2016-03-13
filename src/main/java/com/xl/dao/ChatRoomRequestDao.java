package com.xl.dao;

import com.xl.bean.*;
import com.xl.util.MyRequestUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2016/1/23.
 */
public class ChatRoomRequestDao extends BaseDao<ChatRoomRequest> {

    AccountDao accountDao;
    ChatRoomDao chatRoomDao;

    @Cacheable(value = "ChatRoomRequest", key = "#deviceId")
    public List<ChatRoomRequest> findRequestListByDeviceId(String deviceId, int page) {

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
    public void deleteChatRoomRequestByRoom(String deviceId, ChatRoom room) throws Exception {
        String hql = "From ChatRoomRequest where state = 0 and roomId = ?";
        List<ChatRoomRequest> list = (List<ChatRoomRequest>) getHibernateTemplate().find(hql, room.getId());
        for (int i = 0; i < list.size(); i++) {
            ChatRoomRequest temp = list.get(i);
            //返还金钱
            double coin = temp.getTimes() * room.getPrice();
            Account account = accountDao.getAccountByDeviceId(temp.getDeviceIdForRequester());
            account.setColdCoin(account.getColdCoin() - coin);
            account.setCoin(account.getCoin() + coin);
            accountDao.update(account);

            temp.setState(3);
            getHibernateTemplate().update(temp);
        }
    }

    public ChatRoomRequest findChatRoomRequestByRoom(String deviceId, ChatRoom room) {
        String hql = "From ChatRoomRequest where state = 0 and roomId = ? and deviceIdForRequester = ?";
        try {
            return (ChatRoomRequest) getHibernateTemplate().find(hql, room.getId(), deviceId).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setChatRoomDao(ChatRoomDao chatRoomDao) {
        this.chatRoomDao = chatRoomDao;
    }
}
