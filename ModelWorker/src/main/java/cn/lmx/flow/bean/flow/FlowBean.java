package cn.lmx.flow.bean.flow;

public class FlowBean {
	//流程编号
	private String no;
	//流程名称
	private String name;	
	//单据类型
	private String voucherType;
	//单据类型名称
	private String voucherTypeName;
	//单据显示脚本
	private String viewScript;
	//流程脚本
	private String flowScript;
	//状态
	private String status;
	//不同意处理
	private String disagreeAudit;
	//发布版本
	private String publishVersion;
	//签核类型
	private String auditType;
	//消息模板
	private String message;
	/**
	 * 构造函数
	 */
	public FlowBean() {
		
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
	public String getVoucherTypeName() {
		return voucherTypeName;
	}
	public void setVoucherTypeName(String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
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
	public String getDisagreeAudit() {
		return disagreeAudit;
	}
	public void setDisagreeAudit(String disagreeAudit) {
		this.disagreeAudit = disagreeAudit;
	}
	public String getPublishVersion() {
		return publishVersion;
	}
	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
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
}
