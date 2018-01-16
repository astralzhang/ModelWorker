package cn.lmx.flow.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="SystemProperties")
@Table(name="SystemProperties")
public class SystemProperties {
	@Id
	@Column(name="ID")
	private String id;
	//类型
	@Column(name="Type")
	private String type;
	//处理内容
	@Column(name="ProcessContent")
	private String processContent;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建日期
	@Column(name="CreateTime")
	private String createTime;
	//更新者
	@Column(name="UpdateUser")
	private String updateUser;
	//更新日期
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public SystemProperties() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProcessContent() {
		return processContent;
	}
	public void setProcessContent(String processContent) {
		this.processContent = processContent;
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
