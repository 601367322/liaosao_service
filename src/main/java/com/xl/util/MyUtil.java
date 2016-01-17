package com.xl.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

public class MyUtil {


    public static final String _160x160 = "_160x160.jpg";
    public static final String _320x320 = "_320x320.jpg";
    public static final String _240x1000 = "_240x1000.jpg";
    public static final String _640x640 = "_640x640.jpg";
    public static final String _960x720 = "_720x960.jpg";


    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss");

    public static String getmd5DeviceId(String deviceId) {
        return deviceId.length() > 16 ? MD5.GetMD5Code(deviceId).substring(8, 24)
                : deviceId;
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * 判断是否超出字节长度;
     *
     * @param str
     * @throws UnsupportedEncodingException
     */
    public static boolean isOverStepLength(String str, int maxLen) {
        try {
            if (str.getBytes("GB2312").length > maxLen) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
