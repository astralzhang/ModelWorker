package cn.lmx.flow.entity.flow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="PublishedFlows")
@Table(name="PublishedFlows")
public class PublishedFlows {
	@Id
	@Column(name="ID")
	private String id;
	@Column(name="No")
	private String no;
	@Column(name="Name")
	private String name;
	@Column(name="VoucherType")
	private String voucherType;
	@Column(name="ViewScript")
	private String viewScript;
	@Column(name="FlowScript")
	private String flowScript;
	@Column(name="Status")
	private String status;
	@Column(name="Version")
	private String version;
	//发布版本
	@Column(name="PublishVersion")
	private String publishVersion;
	//不同意处理
	@Column(name="DisagreeAudit")
	private String disagreeAudit;
	//签核模式
	@Column(name="AuditType")
	private String auditType;
	//消息模板
	@Column(name="AuditMessage")
	private String message;
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
	public PublishedFlows() {
		
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
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getViewScript() {
		return viewScript;
	}
	public void setViewScript(String viewScript) {
		this.viewScript = viewScript;
	}
	public String getFlowScript() {
		return flowScript;
	}
	public void setFlowScript(String flowScript) {
		this.flowScript = flowScript;
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
	public String getDisagreeAudit() {
		return disagreeAudit;
	}
	public void setDisagreeAudit(String disagreeAudit) {
		this.disagreeAudit = disagreeAudit;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
