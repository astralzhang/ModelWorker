package cn.lmx.flow.bean.module;

public class PrivilegeBean {
	//功能编码
	private String no;
	//功能名称
	private String name;
	//所属模块
	private String moduleNo;
	//URL
	private String actionUrl;
	//功能类型
	private String privType;
	/**
	 * 构造函数
	 */
	public PrivilegeBean() {
		
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
	public String getModuleNo() {
		return moduleNo;
	}
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getPrivType() {
		return privType;
	}
	public void setPrivType(String privType) {
		this.privType = privType;
	}
}
