package cn.lmx.flow.bean.message;

import java.math.BigDecimal;

public class AuditMessageBean {
	//审批ID
	private String id;
	//单据类型
	private String voucherType;
	//单据类型名称
	private String voucherTypeName;
	//前一位审批人的处理意见
	private String beforeContent;
	//处理意见
	private String content;
	//消息
	private String message;
	//数据主键
	private String dataKey;
	//需处理用户
	private String userNo;
	//需处理用户名
	private String userName;
	//实际处理用户
	private String realUserNo;
	//实际处理用户名
	private String realUserName;
	//处理阶层
	private BigDecimal level;
	//审批属性
	private String auditProp;
	//审批状态
	private String status;
	//处理时间
	private String processTime;
	//签核模式
	private String auditType;
	//工作流实例ID
	private String instanceId;
	/**
	 * 构造函数
	 */
	public AuditMessageBean() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getRealUserNo() {
		return realUserNo;
	}
	public void setRealUserNo(String realUserNo) {
		this.realUserNo = realUserNo;
	}
	public BigDecimal getLevel() {
		return level;
	}
	public void setLevel(BigDecimal level) {
		this.level = level;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealUserName() {
		return realUserName;
	}
	public void setRealUserName(String realUserName) {
		this.realUserName = realUserName;
	}
	public String getAuditProp() {
		return auditProp;
	}
	public void setAuditProp(String auditProp) {
		this.auditProp = auditProp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
