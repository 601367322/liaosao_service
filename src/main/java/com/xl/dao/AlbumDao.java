package com.xl.dao;

import com.xl.bean.Album;

import java.util.List;

/**
 * Created by Shen on 2015/12/13.
 */
public class AlbumDao extends BaseDao {

    public List<Album> getAlbumListByDevicdId(String deviceId) {
        return (List<Album>) getHibernateTemplate().find("From Album where deviceId = '" + deviceId + "'");
    }

    public void deleteAlbumById(Integer id) {
        getHibernateTemplate().delete(getHibernateTemplate().get(Album.class, id));
    }

    public Album queryById(Integer id) {
        return getHibernateTemplate().get(Album.class, id);
    }

}
