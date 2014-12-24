package com.xl.util;

import net.sf.json.JSONObject;

public class MyJSON {

	JSONObject jo = new JSONObject();

	public JSONObject put(String key, Object obj) {
		jo.put(key, obj);
		return jo;
	}

}
