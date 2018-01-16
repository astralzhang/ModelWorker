package cn.lmx.flow.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="DataLogs")
@Entity(name="DataLogs")
public class DataLogs {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//操作类型
	@Column(name="Type")
	private String type;
	//操作名称
	@Column(name="Processor")
	private String processor;
	//数据表
	@Column(name="DataTable")
	private String dataTable;
	//修改前Data
	@Column(name="BeforeData")
	private String beforeData;
	//修改后data
	@Column(name="AfterData")
	private String afterData;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建时间
	@Column(name="CreateTime")
	private String createTime;
	/**
	 * 构造函数
	 */
	public DataLogs() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getDataTable() {
		return dataTable;
	}
	public void setDataTable(String dataTable) {
		this.dataTable = dataTable;
	}
	public String getBeforeData() {
		return beforeData;
	}
	public void setBeforeData(String beforeData) {
		this.beforeData = beforeData;
	}
	public String getAfterData() {
		return afterData;
	}
	public void setAfterData(String afterData) {
		this.afterData = afterData;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
