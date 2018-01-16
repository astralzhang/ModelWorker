package cn.lmx.flow.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="TaskProcessor")
@Table(name="TaskProcessor")
public class TaskProcessor {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//任务ID
	@Column(name="TaskId")
	private String taskId;
	//任务类型
	@Column(name="TaskType")
	private String taskType;
	//执行时间
	@Column(name="ProcessTime")
	private String processTime;
	//URL/SQL
	@Column(name="UrlOrSql")
	private String urlOrSql;
	//参数
	@Column(name="Parameter")
	private String parameter;
	//URL/SQL区分
	@Column(name="UrlSqlFlag")
	private String urlSqlFlag;
	//目标数据库
	@Column(name="Target")
	private String target;
	//是否启动新线程
	@Column(name="StartThread")
	private String startThread;
	//通知标题
	@Column(name="Title")
	private String title;
	//通知模板
	@Column(name="MessageTemplate")
	private String messageTemplate;
	//是否补漏的任务
	@Column(name="Completed")
	private String completed;
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
	public TaskProcessor() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public String getUrlOrSql() {
		return urlOrSql;
	}
	public void setUrlOrSql(String urlOrSql) {
		this.urlOrSql = urlOrSql;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
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
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
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
