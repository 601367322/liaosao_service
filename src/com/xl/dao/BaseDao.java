package com.xl.dao;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

public class BaseDao extends HibernateDaoSupport{

	public Object save(Object obj){
		return getHibernateTemplate().save(obj);
	}
	
	public void saveOrUpdate(Object obj){
		getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public void update(Object obj){
		getHibernateTemplate().update(obj);
	}
}
