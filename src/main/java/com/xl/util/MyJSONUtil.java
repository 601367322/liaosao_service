package com.xl.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xl.socket.StaticUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyJSONUtil {

    public static Object put(String key, Object obj) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(key, obj);
        return MyJSONUtil.beanToJson(result);
    }

    public static Object getErrorInfoJsonObject(String error) {
        return getErrorInfoJsonObject(ResultCode.FAIL, error);
    }


    public static Object getErrorInfoJsonObject(int status, String error) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ResultCode.STATUS, status);
        result.put(ResultCode.INFO, error);
        return MyJSONUtil.beanToJson(result);
    }

    public static Object getErrorJsonObject() {
        return getErrorInfoJsonObject(ResultCode.UNKNOW);
    }

    public static Object getSuccessJsonObject() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ResultCode.STATUS, ResultCode.SUCCESS);
        return MyJSONUtil.beanToJson(result);
    }

    public static Object getSuccessJsonObject(Object content) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ResultCode.STATUS, ResultCode.SUCCESS);
        if (content != null)
            result.put(StaticUtil.CONTENT, content);
        return MyJSONUtil.beanToJson(result);
    }

    public static Object getSuccessJsonObject(String key, Object content) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ResultCode.STATUS, ResultCode.SUCCESS);
        result.put(key, content);
        return MyJSONUtil.beanToJson(result);
    }

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }

    public static <T> ArrayList<T> jsonToList(String json, Class<T> classOfT) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        Gson gson = getGson();
        ArrayList<JsonObject> jsonObjs = gson.fromJson(json, type);
        ArrayList<T> listOfT = null;
        try {
            listOfT = new ArrayList<T>();
            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(gson.fromJson(jsonObj, classOfT));
            }
            return listOfT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String beanToJson(Object bean) {
        return getGson().toJson(bean);
    }

    public static Gson getGson() {
        return new GsonBuilder().disableHtmlEscaping().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }
}
