package cn.lmx.flow.utils;

import java.security.MessageDigest;

public class MD5 {
	/**
	 * MD5加密
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		if (data == null || "".equals(data)) {
			return data;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] inputData = data.getBytes();
			md.update(inputData);
			byte[] md5Bytes = md.digest();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int)md5Bytes[i]) & 0xFF;
				if (val < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(val));
			}
			return sb.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	public static void main(String[] args) {
	
	}
}
