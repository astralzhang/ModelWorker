package cn.lmx.flow.bean.view;

public class OpenWindowBean {
	//窗口编码
	private String no;
	//窗口名称
	private String name;
	//窗口描述
	private String description;
	//SQL文或存储过程
	private String sql;
	//SQL文或存储过程区分
	private String sqlProcFlag;
	//目标数据库
	private String targetDatabase;
	//查询条件
	private String condition;
	//显示列表
	private String viewField;
	/**
	 * 构造函数
	 */
	public OpenWindowBean() {
		
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSqlProcFlag() {
		return sqlProcFlag;
	}
	public void setSqlProcFlag(String sqlProcFlag) {
		this.sqlProcFlag = sqlProcFlag;
	}
	public String getTargetDatabase() {
		return targetDatabase;
	}
	public void setTargetDatabase(String targetDatabase) {
		this.targetDatabase = targetDatabase;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getViewField() {
		return viewField;
	}
	public void setViewField(String viewField) {
		this.viewField = viewField;
	}
}
