package com.xl.bean;

import java.io.Serializable;

/**
 * Created by Shen on 2015/12/20.
 */
public class Account implements Serializable{
    private int id;
    private String deviceId;
    private String zhifubao;
    private String weixin;
    private Double coin = 0.0;

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

    public String getZhifubao() {
        return zhifubao;
    }

    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (deviceId != null ? !deviceId.equals(account.deviceId) : account.deviceId != null) return false;
        if (zhifubao != null ? !zhifubao.equals(account.zhifubao) : account.zhifubao != null) return false;
        if (weixin != null ? !weixin.equals(account.weixin) : account.weixin != null) return false;
        if (coin != null ? !coin.equals(account.coin) : account.coin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (zhifubao != null ? zhifubao.hashCode() : 0);
        result = 31 * result + (weixin != null ? weixin.hashCode() : 0);
        result = 31 * result + (coin != null ? coin.hashCode() : 0);
        return result;
    }
}
