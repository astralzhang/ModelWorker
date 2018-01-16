package cn.lmx.flow.entity.flow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="TableFields")
@Table(name="TableFields")
public class TableFields {
	@Id
	@Column(name="ID")
	private String id;
	@Column(name="TableId")
	private String tableId;
	@Column(name="FieldId")
	private String fieldId;
	@Column(name="FieldName")
	private String fieldName;
	@Column(name="FieldType")
	private String fieldType;
	@Column(name="SearchKey")
	private String searchKey;
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
	public TableFields() {
		
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
