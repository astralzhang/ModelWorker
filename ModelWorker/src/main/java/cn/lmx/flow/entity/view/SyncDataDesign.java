package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="SyncDataDesign")
@Entity(name="SyncDataDesign")
public class SyncDataDesign {
	@Id
	@Column(name="ID")
	private String id;
	//同步编码
	@Column(name="No")
	private String no;
	//同步名称
	@Column(name="Name")
	private String name;
	//同步脚本
	@Column(name="Script")
	private String script;
	//同步模式
	@Column(name="Model")
	private String model;
	//状态
	@Column(name="Status")
	private String status;
	//设计版本
	@Column(name="DesignVersion")
	private String designVersion;
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
	public SyncDataDesign() {
		
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
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDesignVersion() {
		return designVersion;
	}
	public void setDesignVersion(String designVersion) {
		this.designVersion = designVersion;
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
