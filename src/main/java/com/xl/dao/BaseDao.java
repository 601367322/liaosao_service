package com.xl.dao;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class BaseDao<T> extends HibernateDaoSupport {

    public T save(T obj) throws Exception {
        try {
            getHibernateTemplate().save(obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("参数错误");
        }
    }

    public void saveOrUpdate(T obj) throws Exception {
        try {
            getHibernateTemplate().saveOrUpdate(obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("参数错误");
        }
    }

    public void update(T obj) throws Exception {
        try {
            getHibernateTemplate().update(obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("参数错误");
        }
    }

    public void delete(T obj) throws Exception {
        try {
            getHibernateTemplate().delete(obj);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("参数错误");
        }
    }

}
