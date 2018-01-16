package cn.lmx.flow.bean.flow;

public class TableFieldBean {
	//ID
	private String id;
	//表ID
	private String tableId;
	//字段ID
	private String fieldId;
	//字段名称
	private String fieldName;
	//字段类型
	private String fieldType;
	//查询数据主键
	private String searchKey;
	/**
	 * 构造函数
	 */
	public TableFieldBean() {
		
	}
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getTableId() {
		return tableId;
	}
	public final void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public final String getFieldId() {
		return fieldId;
	}
	public final void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public final String getFieldName() {
		return fieldName;
	}
	public final void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public final String getFieldType() {
		return fieldType;
	}
	public final void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
}
