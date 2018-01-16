package cn.lmx.flow.bean.module;

public class UserPermissionBean {
	//权限ID
	private String id;
	//用户编码
	private String userNo;
	//功能编码
	private String itemNo;
	//组编码
	private String groupNo;
	//功能URL
	private String actionUrl;
	/**
	 * 构造函数
	 */
	public UserPermissionBean() {
		
	}
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getUserNo() {
		return userNo;
	}
	public final void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public final String getItemNo() {
		return itemNo;
	}
	public final void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public final String getGroupNo() {
		return groupNo;
	}
	public final void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public final String getActionUrl() {
		return actionUrl;
	}
	public final void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return false;
		}
		String itemNo2 = ((UserPermissionBean)obj).getItemNo();
		if (itemNo == null) {
			if (itemNo2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return itemNo.equals(itemNo2);
		}
	}
}
