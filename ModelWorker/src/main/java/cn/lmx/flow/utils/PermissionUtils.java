package cn.lmx.flow.utils;

import java.util.List;

import cn.lmx.flow.bean.module.UserPermissionBean;

public class PermissionUtils {
	/**
	 * 权限Check
	 * @param url
	 * @return
	 */
	public static String hasPermission(String url) {
		List<UserPermissionBean> permissionList = (List<UserPermissionBean>)UserInfo.get(UserPermissionBean.class.getName());
		if (permissionList == null || permissionList.size() <= 0) {
			return null;
		}
		for (int i = 0; i < permissionList.size(); i++) {
			UserPermissionBean bean = permissionList.get(i);
System.out.println(url);
System.out.println(bean.getActionUrl());
			if (url.equals(bean.getActionUrl())) {
				return bean.getItemNo();
			}
		}
		return null;
	}
}
