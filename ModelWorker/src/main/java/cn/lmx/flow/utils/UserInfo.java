package cn.lmx.flow.utils;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
	//用户信息保存
	private static ThreadLocal<Map<String, Object>> userInfo;
	static {
		userInfo = new ThreadLocal<Map<String, Object>>();
	}
	/**
	 * 增加信息
	 * @param name
	 * @param data
	 */
	public static void put(String name, Object data) {
		Map<String, Object> map = userInfo.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			userInfo.set(map);
		}
		map.put(name, data);
	}
	/**
	 * 取得信息
	 * @param name
	 * @return
	 */
	public static Object get(String name) {
		Map<String, Object> map = userInfo.get();
		if (map == null) {
			return null;
		}
		return map.get(name);
	}
	/**
	 * 删除信息
	 * @param name
	 */
	public static void remove(String name) {
		Map<String, Object> map = userInfo.get();
		if (map == null) {
			return;
		}
		map.remove(name);
	}
}
