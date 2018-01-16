package cn.lmx.flow.bean.module;

public class GroupBean {
	//组编码
	private String groupNo;
	//组名称
	private String groupName;
	//组描述
	private String description;
	/**
	 * 构造函数
	 */
	public GroupBean() {
		
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
