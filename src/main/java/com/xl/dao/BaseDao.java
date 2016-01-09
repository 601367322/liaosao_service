package com.xl.dao;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class BaseDao<T> extends HibernateDaoSupport {

    public Object save(Object obj) throws Exception {
        try {
            return getHibernateTemplate().save(obj);
        } catch (Exception e) {
            throw new Exception("参数错误");
        }
    }

    public void saveOrUpdate(Object obj) throws Exception {
        try {
            getHibernateTemplate().saveOrUpdate(obj);
        } catch (Exception e) {
            throw new Exception("参数错误");
        }
    }

    public void update(Object obj) throws Exception {
        try {
            getHibernateTemplate().update(obj);
        } catch (Exception e) {
            throw new Exception("参数错误");
        }
    }

}
