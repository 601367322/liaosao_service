package com.xl.bean;

/**
 * Created by Shen on 2015/10/10.
 */
public class Pay {
    private String out_trade_no;
    private String name;
    private String body;
    private String create_time;
    private String transaction_id;
    private String pay_type;
    private Double total_fee;
    private String trade_state;


    public Pay() {
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public Double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Double total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public enum PayType{
        ALIPAY("ALIPAY"),WECHATPAY("WECHATPAY"),TIXIAN("TIXIAN"),CHATREQUEST("CHATREQUEST");

        public String result;

        PayType(String result) {
            this.result = result;
        }

        public String getResult(){
            return result;
        }
    }
}
