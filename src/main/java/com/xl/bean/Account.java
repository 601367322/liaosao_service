package com.xl.bean;

import java.io.Serializable;

/**
 * Created by Shen on 2015/12/20.
 */
public class Account implements Serializable{
    private int id;
    private String deviceId;
    private String zhifubao;
    private Double coin = 0.0;
    private Double coldCoin = 0.0;

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


    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public Double getColdCoin() {
        return coldCoin;
    }

    public void setColdCoin(Double coldCoin) {
        this.coldCoin = coldCoin;
    }
}
