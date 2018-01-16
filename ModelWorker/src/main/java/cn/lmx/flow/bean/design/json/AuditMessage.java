package cn.lmx.flow.bean.design.json;

public class AuditMessage {
	//状态
	private String status;
	//类型
	private String type;
	//消息模板类型
	private String messageType;
	//消息模板
	private String message;
	/**
	 * 构造函数
	 */
	public AuditMessage() {
		
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
