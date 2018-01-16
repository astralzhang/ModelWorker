package cn.lmx.flow.entity.task;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Tasks")
@Table(name="Tasks")
public class Tasks {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//任务名称
	@Column(name="Name")
	private String name;
	//任务描述
	@Column(name="Description")
	private String description;
	//任务类型
	@Column(name="TaskType")
	private String taskType;
	//任务执行频率
	@Column(name="Frequency")
	private BigDecimal frequency;
	//频率单位
	@Column(name="Unit")
	private String unit;
	//开始时间
	@Column(name="StartTime")
	private String startTime;
	//结束时间
	@Column(name="EndTime")
	private String endTime;
	//执行URL/SQL
	@Column(name="UrlOrSql")
	private String urlOrSql;
	//URL/SQL区分
	@Column(name="UrlSqlFlag")
	private String urlSqlFlag;
	//目标数据库
	@Column(name="Target")
	private String target;
	//是否启动新线程
	@Column(name="StartThread")
	private String startThread;
	//参数
	@Column(name="Parameter")
	private String parameter;
	//消息标题
	@Column(name="Title")
	private String title;
	//消息模板
	@Column(name="MessageTemplate")
	private String messageTemplate;
	//是否补漏
	@Column(name="CompleteMiss")
	private String completeMiss;
	//状态
	@Column(name="Status")
	private String status;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建日期
	@Column(name="CreateTime")
	private String createTime;
	//更新者
	@Column(name="UpdateUser")
	private String updateUser;
	//更新日期
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public Tasks() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public BigDecimal getFrequency() {
		return frequency;
	}
	public void setFrequency(BigDecimal frequency) {
		this.frequency = frequency;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getUrlOrSql() {
		return urlOrSql;
	}
	public void setUrlOrSql(String urlOrSql) {
		this.urlOrSql = urlOrSql;
	}
	public String getUrlSqlFlag() {
		return urlSqlFlag;
	}
	public void setUrlSqlFlag(String urlSqlFlag) {
		this.urlSqlFlag = urlSqlFlag;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getStartThread() {
		return startThread;
	}
	public void setStartThread(String startThread) {
		this.startThread = startThread;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessageTemplate() {
		return messageTemplate;
	}
	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
	public String getCompleteMiss() {
		return completeMiss;
	}
	public void setCompleteMiss(String completeMiss) {
		this.completeMiss = completeMiss;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
