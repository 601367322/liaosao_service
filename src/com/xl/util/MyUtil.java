package com.xl.util;

import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

public class MyUtil {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	public static String toJson(Object obj) {
		return JSONObject.fromObject(obj,
				DefaultDefaultValueProcessor.getJsonConfig()).toString();
	}
	
	public static String getmd5DeviceId(String deviceId) {
		return MD5.GetMD5Code(deviceId).substring(8, 24);
	}
}
