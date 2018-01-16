package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ReportDesign")
@Entity(name="ReportDesign")
public class ReportDesign {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//报表编码
	@Column(name="No")
	private String no;
	//报表名称
	@Column(name="Name")
	private String name;
	//所属模块
	@Column(name="ModuleNo")
	private String moduleNo;
	//画面脚本
	@Column(name="ViewScript")
	private String viewScript;
	//状态
	@Column(name="Status")
	private String status;
	//版本
	@Column(name="Version")
	private String version;
	//发布版本
	@Column(name="PublishVersion")
	private String publishVersion;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建时间
	@Column(name="CreateTime")
	private String createTime;
	//修改者
	@Column(name="UpdateUser")
	private String updateUser;
	//修改时间
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public ReportDesign() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getViewScript() {
		return viewScript;
	}
	public void setViewScript(String viewScript) {
		this.viewScript = viewScript;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPublishVersion() {
		return publishVersion;
	}
	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
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
