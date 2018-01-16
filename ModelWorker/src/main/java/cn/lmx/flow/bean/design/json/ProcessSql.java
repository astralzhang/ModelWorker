package cn.lmx.flow.bean.design.json;

public class ProcessSql {
	//目标数据库
	private String target;
	//SQL类型
	private String sqlType;
	//处理时点
	private String section;
	//处理SQL
	private String sql;
	/**
	 * 构造函数
	 */
	public ProcessSql() {
		
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
}
