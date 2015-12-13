package com.xl.util;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;

public class MyUtil {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	public static String toJson(Object obj) {
		return JSONObject.fromObject(obj,
				DefaultDefaultValueProcessor.getJsonConfig()).toString();
	}

    public static String toJsonNoNull(Object obj) {
        return JSONObject.fromObject(obj).toString();
    }
	
	public static String getmd5DeviceId(String deviceId) {
        return deviceId.length() > 16 ? MD5.GetMD5Code(deviceId).substring(8, 24)
                : deviceId;
	}

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

}
