package com.xl.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shen on 2015/11/28.
 */
public class Test1 {


    private static int TIME_OUT = 10000;

    private static String STRING_EMPTY = "";
    private static String APP_ID = STRING_EMPTY;
    private static String REST_API_KEY = STRING_EMPTY;
    private static String MASTER_KEY = STRING_EMPTY;

    private static final String BMOB_APP_ID_TAG = "X-Bmob-Application-Id";
    private static final String BMOB_REST_KEY_TAG = "X-Bmob-REST-API-Key";
    private static final String BMOB_MASTER_KEY_TAG = "X-Bmob-Master-Key";
    private static final String CONTENT_TYPE_TAG = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String METHOD_POST = "POST";
    private static final String UTF8 = "UTF-8";
    public static void main(String[] args) {
            HttpURLConnection conn = null;
            String mURL = "http://www.bjqlr.com/admin/user/update_status.php?id=114073&status=0";
            try {
                conn = connectionCommonSetting(conn, new URL(mURL), METHOD_POST);
                conn.connect();
                System.out.println(getResultFromConnection(conn));
                conn.disconnect();
            } catch (Exception e){
                e.printStackTrace();
            }
    }

    private static HttpURLConnection connectionCommonSetting(HttpURLConnection conn, URL url, String method) throws IOException {
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setReadTimeout(TIME_OUT);

        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);

        conn.setRequestProperty(BMOB_APP_ID_TAG, APP_ID);
        conn.setRequestProperty(BMOB_REST_KEY_TAG, REST_API_KEY);
        if(!MASTER_KEY.equals(STRING_EMPTY)){
            conn.setRequestProperty(BMOB_MASTER_KEY_TAG, MASTER_KEY);
        }

        conn.setRequestProperty(CONTENT_TYPE_TAG, CONTENT_TYPE_JSON);
        return conn;
    }

    private static String getResultFromConnection(HttpURLConnection conn) throws UnsupportedEncodingException, IOException{
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),UTF8));
        String line;
        while ((line = reader.readLine()) != null){
            result.append(line);
        }
        reader.close();
        return result.toString();
    }
}
