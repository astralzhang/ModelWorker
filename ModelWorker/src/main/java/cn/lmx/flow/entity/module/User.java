package cn.lmx.flow.entity.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity(name="User")
@Table(name="Users")
public class User {
	@Id
	@Column(name="UserNo")
	private String userNo;
	@Column(name="UserName")
	private String userName;
	@Column(name="UserPassword")
	private String userPassword;
	@Column(name="MailAddress")
	private String mailAddress;
	@Column(name="Status")
	private String status;
	@Column(name="CreateUser")
	private String createUser;
	@Column(name="CreateTime")
	private String createTime;
	@Column(name="UpdateUser")
	private String updateUser;
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public User() {
		
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
	public final String getCreateUser() {
		return createUser;
	}
	public final void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public final String getCreateTime() {
		return createTime;
	}
	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public final String getUpdateUser() {
		return updateUser;
	}
	public final void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public final String getUpdateTime() {
		return updateTime;
	}
	public final void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
