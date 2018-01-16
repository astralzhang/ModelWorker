package cn.lmx.flow.bean.report.json;

public class ConditionField {
	//显示名
	private String label;
	//字段
	private String field;
	//别名
	private String alias;
	//编辑类型
	private String editor;
	//比较方法
	private String compare;
	//开窗编码
	private String openWinNo;
	//SQL函数
	private String sqlFunc;
	//目标数据库
	private String target;
	//默认值设定类型
	private String defaultType;
	//默认值设定方法
	private String defaultValue;
	//默认值目标数据库
	private String defaultTarget;
	//默认值映射字段
	private String defaultField;
	/**
	 * 构造函数
	 */
	public ConditionField() {
		
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
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getCompare() {
		return compare;
	}
	public void setCompare(String compare) {
		this.compare = compare;
	}
	public String getOpenWinNo() {
		return openWinNo;
	}
	public void setOpenWinNo(String openWinNo) {
		this.openWinNo = openWinNo;
	}
	public String getSqlFunc() {
		return sqlFunc;
	}
	public void setSqlFunc(String sqlFunc) {
		this.sqlFunc = sqlFunc;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
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
