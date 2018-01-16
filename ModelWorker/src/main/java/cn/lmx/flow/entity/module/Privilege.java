package cn.lmx.flow.entity.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Privilege")
@Table(name = "Privileges")
public class Privilege {
	//功能编码
	@Id
	@Column(name="No")
	private String no;
	//功能名称
	@Column(name="Name")
	private String name;
	//所属模块
	@Column(name="ModuleNo")
	private String moduleNo;
	//URL
	@Column(name="ActionUrl")
	private String actionUrl;
	//功能类型
	@Column(name="PrivType")
	private String privType;
	//父功能编码
	@Column(name="ParentNo")
	private String parentNo;
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
	public Privilege() {
		
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModuleNo() {
		return moduleNo;
	}
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getPrivType() {
		return privType;
	}
	public void setPrivType(String privType) {
		this.privType = privType;
	}
	public String getParentNo() {
		return parentNo;
	}
	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
