package cn.lmx.flow.entity.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Permission")
@Table(name="Permission")
public class Permission {
	@Id
	@Column(name="ID")
	private String id;
	@Column(name="UserNo")
	private String userNo;
	@Column(name="ItemNo")
	private String itemNo;
	@Column(name="UserType")
	private String userType;
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
	public Permission() {
		
	}
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getUserNo() {
		return userNo;
	}
	public final void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public final String getItemNo() {
		return itemNo;
	}
	public final void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public final String getUserType() {
		return userType;
	}
	public final void setUserType(String userType) {
		this.userType = userType;
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
