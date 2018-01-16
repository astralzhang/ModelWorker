package cn.lmx.flow.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.lmx.flow.bean.module.UserBean;

public class AuditMessageUtils {
	//用户一栏
	private List<UserBean> userList;
	/**
	 * 构造函数
	 * @param userList
	 */
	public AuditMessageUtils(List<UserBean> userList) {
		this.userList = userList;
	}
	/**
	 * 取得用户一栏
	 * @param userNo
	 * @return
	 */
	public String GetUserName(String userNo) {
		if (userNo == null || "".equals(userNo)) {
			return "";
		}
		if (userList == null) {
			return userNo;
		}
		for (int i = 0; i < userList.size(); i++) {
			UserBean userBean = userList.get(i);
			if (userNo.equals(userBean.getUserNo())) {
				return userBean.getUserName();
			}
		}
		return userNo;
	}
	/**
	 * 格式化日期
	 * @param sDate
	 * @param format
	 * @return
	 */
	public String FormatDate(String sDate, String oldFormat, String newFormat) {
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat oldSdf = new SimpleDateFormat(oldFormat);
			c.setTime(oldSdf.parse(sDate));
			SimpleDateFormat newSdf = new SimpleDateFormat(newFormat);
			return newSdf.format(c.getTime());
		} catch (ParseException e) {
			return sDate;
		}
	}
}
