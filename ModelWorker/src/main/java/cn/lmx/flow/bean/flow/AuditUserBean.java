package cn.lmx.flow.bean.flow;

public class AuditUserBean {
	//用户编码
	private String userNo;
	//用户名称
	private String userName;
	//实际处理用户编码
	private String realUserNo;
	//实际处理用户名
	private String realUserName;
	//审核状态(0:未处理、1:否决、2:同意、4:取消)
	private String auditStatus;
	//人员状态(0:正常、1:加签)
	private String userStatus;
	//审核时间
	private String auditTime;
	/**
	 * 构造函数
	 */
	public AuditUserBean() {
		
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealUserNo() {
		return realUserNo;
	}
	public void setRealUserNo(String realUserNo) {
		this.realUserNo = realUserNo;
	}
	public String getRealUserName() {
		return realUserName;
	}
	public void setRealUserName(String realUserName) {
		this.realUserName = realUserName;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
}
