package com.xl.bean;

/**
 * Created by Shen on 2015/12/13.
 */
public class Album {
    private int id;
    private String deviceId;
    private String path;

    public Album() {
    }

    public Album(int id, String deviceId, String path) {
        this.id = id;
        this.deviceId = deviceId;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (id != album.id) return false;
        if (deviceId != null ? !deviceId.equals(album.deviceId) : album.deviceId != null) return false;
        if (path != null ? !path.equals(album.path) : album.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
