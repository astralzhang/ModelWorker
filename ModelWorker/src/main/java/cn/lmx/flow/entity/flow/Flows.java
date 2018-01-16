package cn.lmx.flow.entity.flow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Flows")
@Table(name="Flows")
public class Flows {
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
	public Flows() {
		
	}
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getNo() {
		return no;
	}
	public final void setNo(String no) {
		this.no = no;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final String getVoucherType() {
		return voucherType;
	}
	public final void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public final String getViewScript() {
		return viewScript;
	}
	public final void setViewScript(String viewScript) {
		this.viewScript = viewScript;
	}
	public final String getFlowScript() {
		return flowScript;
	}
	public final void setFlowScript(String flowScript) {
		this.flowScript = flowScript;
	}
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String status) {
		this.status = status;
	}
	public final String getVersion() {
		return version;
	}
	public final void setVersion(String version) {
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
