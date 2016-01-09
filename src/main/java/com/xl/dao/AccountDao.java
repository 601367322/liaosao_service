package com.xl.dao;

import com.xl.bean.Account;

import java.util.List;

/**
 * Created by Shen on 2015/12/20.
 */
public class AccountDao extends BaseDao {

    public Account getAccountByDeviceId(String deviceId) {
        List<Account> list = (List<Account>) getHibernateTemplate().find("From Account where deviceId = '" + deviceId + "'");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
