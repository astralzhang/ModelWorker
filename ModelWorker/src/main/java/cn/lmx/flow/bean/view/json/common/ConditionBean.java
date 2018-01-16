package cn.lmx.flow.bean.view.json.common;

public class ConditionBean {
	//数据来源
	private String source;
	//显示名
	private String label;
	//字段
	private String field;
	//字段别名
	private String alias;
	//比较符
	private String compare;
	//sql文
	private String sql;
	//编辑器
	private String editor;
	//目标数据库
	private String target;
	//sql、proc区分
	private String sqlProc;
	//默认值设定类型
	private String defaultType;
	//默认值设定方法
	private String defaultValue;
	//默认值设定目标数据库
	private String defaultTarget;
	//默认值设定字段映射
	private String defaultField;
	/**
	 * 构造函数
	 */
	public ConditionBean() {
		
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getCompare() {
		return compare;
	}
	public void setCompare(String compare) {
		this.compare = compare;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSqlProc() {
		return sqlProc;
	}
	public void setSqlProc(String sqlProc) {
		this.sqlProc = sqlProc;
	}
	public String getDefaultType() {
		return defaultType;
	}
	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDefaultTarget() {
		return defaultTarget;
	}
	public void setDefaultTarget(String defaultTarget) {
		this.defaultTarget = defaultTarget;
	}
	public String getDefaultField() {
		return defaultField;
	}
	public void setDefaultField(String defaultField) {
		this.defaultField = defaultField;
	}
}
