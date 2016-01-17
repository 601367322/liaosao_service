package com.xl.dao;

import com.xl.bean.Album;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by Shen on 2015/12/13.
 */
public class AlbumDao extends BaseDao<Album> {

    @Cacheable(value = "Album", key = "#deviceId")
    public List<Album> getAlbumListByDevicdId(String deviceId) {
        return (List<Album>) getHibernateTemplate().find("From Album where deviceId = '" + deviceId + "'");
    }

    @CacheEvict(value = "Album", key = "#deviceId")
    public void deleteAlbumById(String deviceId, Integer id) {
        getHibernateTemplate().delete(getHibernateTemplate().get(Album.class, id));
    }

    @Cacheable(value = "Album", key = "#deviceId")
    public Album queryById(String deviceId, Integer id) {
        return getHibernateTemplate().get(Album.class, id);
    }

    @CacheEvict(value = "Album", key = "#obj.deviceId")
    @Override
    public Object save(Album obj) throws Exception {
        return super.save(obj);
    }

    @CacheEvict(value = "Album", key = "#obj.deviceId")
    @Override
    public void saveOrUpdate(Album obj) throws Exception {
        super.saveOrUpdate(obj);
    }

    @CacheEvict(value = "Album", key = "#obj.deviceId")
    @Override
    public void update(Album obj) throws Exception {
        super.update(obj);
    }
}
