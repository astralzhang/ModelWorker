package cn.lmx.flow.bean.task;

public class TaskProcessorBean {
	private String id;
	//任务ID
	private String taskId;
	//任务类型
	private String taskType;
	//执行时间
	private String processTime;
	//URL/SQL
	private String urlOrSql;
	//参数
	private String parameter;
	//URL/SQL区分
	private String urlSqlFlag;
	//目标数据库
	private String target;
	//是否启动新线程
	private String startThread;
	//通知标题
	private String title;
	//通知模板
	private String messageTemplate;
	/**
	 * 构造函数
	 */
	public TaskProcessorBean() {
		
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
}
