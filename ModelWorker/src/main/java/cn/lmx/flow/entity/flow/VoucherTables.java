package cn.lmx.flow.entity.flow;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="VoucherTables")
@Table(name="VoucherTables")
public class VoucherTables {
	@Id
	@Column(name="ID")
	private String id;
	@Column(name="TypeNo")
	private String typeNo;
	@Column(name="TableId")
	private String tableId;
	@Column(name="LocalTableId")
	private String localTableId;
	@Column(name="SelectSql")
	private String selectSql;
	@Column(name="TableName")
	private String tableName;
	//表类型（U:用户表、B:业务数据表)
	@Column(name="TableType")
	private String tableType;
	//数据类型(M：数据主表、D:数据明细表)
	@Column(name="DataType")
	private String dataType;
	//数据库类型(W:工作流数据库、B:业务数据库)
	@Column(name="DatabaseType")
	private String databaseType;
	@Column(name="UserNoField")
	private String userNoField;
	//条件使用第几行数据
	@Column(name="ConditionRowIndex")
	private BigDecimal conditionRowIndex;
	@Column(name="CreateUser")
	private String createUser;
	@Column(name="CreateTime")
	private String createTime;
	@Column(name="UpdateUser")
	private String updateUser;
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public VoucherTables() {;
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
	public String getLocalTableId() {
		return localTableId;
	}
	public void setLocalTableId(String localTableId) {
		this.localTableId = localTableId;
	}
	public String getSelectSql() {
		return selectSql;
	}
	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public final String getDatabaseType() {
		return databaseType;
	}
	public final void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	public String getUserNoField() {
		return userNoField;
	}
	public void setUserNoField(String userNoField) {
		this.userNoField = userNoField;
	}
	public BigDecimal getConditionRowIndex() {
		return conditionRowIndex;
	}
	public void setConditionRowIndex(BigDecimal conditionRowIndex) {
		this.conditionRowIndex = conditionRowIndex;
	}
	public final String getCreateUser() {
		return createUser;
	}
	public final void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public final String getCreateTime() {
		return createTime;
	}
	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public final String getUpdateUser() {
		return updateUser;
	}
	public final void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public final String getUpdateTime() {
		return updateTime;
	}
	public final void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
