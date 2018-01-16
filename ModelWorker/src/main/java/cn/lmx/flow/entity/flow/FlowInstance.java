package cn.lmx.flow.entity.flow;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="FlowInstance")
@Entity(name="FlowInstance")
public class FlowInstance {
	@Id
	@Column(name="ID")
	private String id;
	@Column(name="DataKey")
	private String dataKey;
	@Column(name="FlowNo")
	private String flowNo;
	@Column(name="FlowVersion")
	private String flowVersion;
	@Column(name="Status")
	private String status;
	//单据类型
	@Column(name="VoucherType")
	private String voucherType;
	//单据类型名称
	@Column(name="VoucherTypeName")
	private String voucherTypeName;
	//当前审批阶层
	@Column(name="CurrentLevel")
	private BigDecimal currentLevel;
	//签核模式
	@Column(name="AuditType")
	private String auditType;
	//画面编码
	@Column(name="ViewNo")
	private String viewNo;
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
	public FlowInstance() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(String flowVersion) {
		this.flowVersion = flowVersion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherTypeName() {
		return voucherTypeName;
	}
	public void setVoucherTypeName(String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
	}
	public BigDecimal getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(BigDecimal currentLevel) {
		this.currentLevel = currentLevel;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getViewNo() {
		return viewNo;
	}
	public void setViewNo(String viewNo) {
		this.viewNo = viewNo;
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
