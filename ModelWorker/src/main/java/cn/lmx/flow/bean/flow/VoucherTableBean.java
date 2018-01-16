package cn.lmx.flow.bean.flow;

public class VoucherTableBean {
	//ID
	private String id;
	//单据类型
	private String typeNo;
	//表ID
	private String tableId;
	//表名称
	private String tableName;
	//表类型
	private String tableType;
	//数据库类型
	private String databaseType;
	/**
	 * 构造函数
	 */
	public VoucherTableBean() {
		
	}
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getTypeNo() {
		return typeNo;
	}
	public final void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}
	public final String getTableId() {
		return tableId;
	}
	public final void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public final String getTableName() {
		return tableName;
	}
	public final void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public final String getDatabaseType() {
		return databaseType;
	}
	public final void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
}
