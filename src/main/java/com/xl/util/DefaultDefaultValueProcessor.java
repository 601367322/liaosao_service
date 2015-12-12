package com.xl.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

public class DefaultDefaultValueProcessor implements DefaultValueProcessor {
	public Object getDefaultValue(Class type) {
		return null;
	}

	public static JsonConfig getJsonConfig() {
		JsonConfig jConfig = new JsonConfig();
		jConfig.registerDefaultValueProcessor(Integer.class, new DefaultDefaultValueProcessor());
		jConfig.registerDefaultValueProcessor(String.class, new DefaultDefaultValueProcessor());
		jConfig.registerDefaultValueProcessor(Double.class, new DefaultDefaultValueProcessor());
		return jConfig;
	}
}