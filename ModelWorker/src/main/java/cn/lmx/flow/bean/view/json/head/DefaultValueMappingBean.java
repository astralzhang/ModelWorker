package cn.lmx.flow.bean.view.json.head;

public class DefaultValueMappingBean {
	//数据表
	private String table;
	//SQL字段
	private String sqlField;
	//画面字段
	private String viewField;
	/**
	 * 构造函数
	 */
	public DefaultValueMappingBean() {
		
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getSqlField() {
		return sqlField;
	}
	public void setSqlField(String sqlField) {
		this.sqlField = sqlField;
	}
	public String getViewField() {
		return viewField;
	}
	public void setViewField(String viewField) {
		this.viewField = viewField;
	}
}
