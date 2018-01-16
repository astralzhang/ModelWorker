package cn.lmx.flow.bean.design.flow;

public class AuditNode extends Node {
	//节点描述
	private String desc;
	//审核用户
	private String auditUserCondition;
	//审核处理SQL
	private String auditProcessSql;
	/**
	 * 构造函数
	 */
	public AuditNode() {

	}
	public final String getDesc() {
		return desc;
	}
	public final void setDesc(String desc) {
		this.desc = desc;
	}
	public final String getAuditUserCondition() {
		return auditUserCondition;
	}
	public final void setAuditUserCondition(String auditUserCondition) {
		this.auditUserCondition = auditUserCondition;
	}
	public String getAuditProcessSql() {
		return auditProcessSql;
	}
	public void setAuditProcessSql(String auditProcessSql) {
		this.auditProcessSql = auditProcessSql;
	}
}
