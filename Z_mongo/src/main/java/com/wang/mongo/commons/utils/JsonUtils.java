package com.wang.mongo.commons.utils;

import com.alibaba.fastjson.JSON;

/**
 * 序列化和反序列化
 * @author wxe
 * @since 1.0.0
 */
public final class JsonUtils {

	public static String toJson(Object o) {
		return JSON.toJSONString(o);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

}
