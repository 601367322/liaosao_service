package com.xl.util;

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
        JSONObject jo = new JSONObject();
        jo.put(ResultCode.STATUS, ResultCode.FAIL);
        return jo;
    }
}
