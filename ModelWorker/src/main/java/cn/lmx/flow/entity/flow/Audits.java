package cn.lmx.flow.entity.flow;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="Audits")
@Entity(name="Audits")
public class Audits {
	@Id
	@Column(name="ID")
	private String id;
	@Column(name="InstanceId")
	private String instanceId;
	//单据类型
	@Column(name="VoucherType")
	private String voucherType;
	//单据类型名称
	@Column(name="VoucherTypeName")
	private String voucherTypeName;
	@Column(name="AuditSection")
	private String auditSection;
	@Column(name="PreAuditSection")
	private String preAuditSection;
	//审批层级
	@Column(name="Level")
	private BigDecimal level;
	@Column(name="UserNo")
	private String userNo;
	//前一位审批人的审批意见
	@Column(name="BeforeContent")
	private String beforeContent;
	@Column(name="Content")
	private String content;
	@Column(name="RealUserNo")
	private String realUserNo;
	//处理SQL
	@Column(name="ProcessSql")
	private String processSql;
	@Column(name="Status")
	private String status;
	//审核属性
	@Column(name="AuditProp")
	private String auditProp;
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
	public Audits() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
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
	public String getAuditSection() {
		return auditSection;
	}
	public void setAuditSection(String auditSection) {
		this.auditSection = auditSection;
	}
	public String getPreAuditSection() {
		return preAuditSection;
	}
	public void setPreAuditSection(String preAuditSection) {
		this.preAuditSection = preAuditSection;
	}
	public BigDecimal getLevel() {
		return level;
	}
	public void setLevel(BigDecimal level) {
		this.level = level;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getBeforeContent() {
		return beforeContent;
	}
	public void setBeforeContent(String beforeContent) {
		this.beforeContent = beforeContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRealUserNo() {
		return realUserNo;
	}
	public void setRealUserNo(String realUserNo) {
		this.realUserNo = realUserNo;
	}
	public String getProcessSql() {
		return processSql;
	}
	public void setProcessSql(String processSql) {
		this.processSql = processSql;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuditProp() {
		return auditProp;
	}
	public void setAuditProp(String auditProp) {
		this.auditProp = auditProp;
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
