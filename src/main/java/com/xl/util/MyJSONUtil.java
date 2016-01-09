package com.xl.util;

import com.xl.socket.StaticUtil;
import net.sf.json.JSONObject;

public class MyJSONUtil {

    public static JSONObject put(String key, Object obj) {
        JSONObject jo = new JSONObject();
        jo.put(key, obj);
        return jo;
    }

    public static JSONObject getErrorInfoJsonObject(String error) {
        JSONObject jo = new JSONObject();
        jo.put(ResultCode.STATUS, ResultCode.FAIL);
        jo.put(ResultCode.INFO, error);
        return jo;
    }

    public static JSONObject getErrorJsonObject() {
        return getErrorInfoJsonObject(ResultCode.UNKNOW);
    }

    public static JSONObject getSuccessJsonObject() {
        JSONObject jo = new JSONObject();
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        return jo;
    }

    public static JSONObject getSuccessJsonObject(Object content) {
        JSONObject jo = new JSONObject();
        jo.put(ResultCode.STATUS, ResultCode.SUCCESS);
        jo.put(StaticUtil.CONTENT, JSONObject.fromObject(content,DefaultDefaultValueProcessor.getJsonConfig()));
        return jo;
    }
}
