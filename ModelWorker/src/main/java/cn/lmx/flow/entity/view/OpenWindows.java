package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="OpenWindows")
@Table(name="OpenWindows")
public class OpenWindows {
	//窗口编码
	@Id
	@Column(name="No")
	private String no;
	//窗口名称
	@Column(name="Name")
	private String name;
	//窗口描述
	@Column(name="Description")
	private String description;
	//SQL文或存储过程
	@Column(name="SqlProc")
	private String sql;
	//SQL文或存储过程区分
	@Column(name="SqlProcFlag")
	private String sqlProcFlag;
	//目标数据库
	@Column(name="TargetDatabase")
	private String targetDatabase;
	//查询条件
	@Column(name="SearchCondition")
	private String condition;
	//显示列表
	@Column(name="ViewField")
	private String viewField;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建时间
	@Column(name="CreateTime")
	private String createTime;
	//修改者
	@Column(name="UpdateUser")
	private String updateUser;
	//修改时间
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public OpenWindows() {
		
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
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
