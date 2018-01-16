package cn.lmx.flow.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="TaskProcessHistory")
@Table(name="TaskProcessHistory")
public class TaskProcessHistory {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//任务ID
	@Column(name="TaskID")
	private String taskId;
	//任务类型
	@Column(name="TaskType")
	private String taskType;
	//执行开始时间
	@Column(name="BeginTime")
	private String beginTime;
	//执行结束时间
	@Column(name="EndTime")
	private String endTime;
	//执行URL/SQL
	@Column(name="UrlOrSql")
	private String urlOrSql;
	//参数
	@Column(name="Parameter")
	private String parameter;
	//目标数据库
	@Column(name="Target")
	private String target;
	//线程
	@Column(name="Thread")
	private String thread;
	//URL/SQL区分
	@Column(name="UrlSqlFlag")
	private String urlSqlFlag;
	//标题
	@Column(name="Title")
	private String title;
	//消息
	@Column(name="Message")
	private String message;
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
	public TaskProcessHistory() {
		
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
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
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
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getThread() {
		return thread;
	}
	public void setThread(String thread) {
		this.thread = thread;
	}
	public String getUrlSqlFlag() {
		return urlSqlFlag;
	}
	public void setUrlSqlFlag(String urlSqlFlag) {
		this.urlSqlFlag = urlSqlFlag;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
