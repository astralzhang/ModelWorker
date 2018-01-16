package cn.lmx.flow.bean.module;

public class UserBean {
	//用户编码
	private String userNo;
	//用户名称
	private String userName;
	//用户密码
	private String userPassword;
	//Email地址
	private String mailAddress;
	//用户状态
	private String status;
	/**
	 * 构造函数
	 */
	public UserBean() {
		
	}
	public final String getUserNo() {
		return userNo;
	}
	public final void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public final String getUserName() {
		return userName;
	}
	public final void setUserName(String userName) {
		this.userName = userName;
	}
	public final String getUserPassword() {
		return userPassword;
	}
	public final void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public final String getMailAddress() {
		return mailAddress;
	}
	public final void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String status) {
		this.status = status;
	}
}
