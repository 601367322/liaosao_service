package com.xl.dao;

import com.xl.bean.VipCoin;

/**
 * Created by Shen on 2015/10/11.
 */
public class VipCoinDao extends BaseDao {


    public VipCoin getCoinById(int id){
        return getHibernateTemplate().get(VipCoin.class, id);
    }
}
