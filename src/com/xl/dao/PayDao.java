package com.xl.dao;

import com.xl.bean.Pay;

/**
 * Created by Shen on 2015/10/10.
 */
public class PayDao extends BaseDao {

    public Pay getPay(String orderId) {
        return getHibernateTemplate().get(Pay.class, orderId);
    }
}
