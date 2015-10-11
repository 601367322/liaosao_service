package com.xl.bean;

/**
 * Created by Shen on 2015/10/11.
 */
public class VipCoin {
    private int id;
    private String name;
    private Double price;
    private Integer month;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VipCoin vipCoin = (VipCoin) o;

        if (id != vipCoin.id) return false;
        if (name != null ? !name.equals(vipCoin.name) : vipCoin.name != null) return false;
        if (price != null ? !price.equals(vipCoin.price) : vipCoin.price != null) return false;
        if (month != null ? !month.equals(vipCoin.month) : vipCoin.month != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (month != null ? month.hashCode() : 0);
        return result;
    }
}
