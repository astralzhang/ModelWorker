package cn.lmx.flow.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="TaskProcessError")
@Table(name="TaskProcessError")
public class TaskProcessError {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//TaskId
	@Column(name="TaskId")
	private String taskId;
	//processId
	@Column(name="ProcessId")
	private String processId;
	//异常原因
	@Column(name="ErrorMessage")
	private String errorMessage;
	//创建时间
	@Column(name="CreateTime")
	private String createTime;
	/**
	 * 构造函数
	 */
	public TaskProcessError() {
		
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
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
