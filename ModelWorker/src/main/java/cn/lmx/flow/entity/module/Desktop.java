package cn.lmx.flow.entity.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="Desktop")
@Entity(name="Desktop")
public class Desktop {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//用户编码
	@Column(name="UserNo")
	private String userNo;
	//功能编码
	@Column(name="ItemNo")
	private String itemNo;
	//功能名称
	@Column(name="ItemName")
	private String itemName;
	//访问URL
	@Column(name="ActionUrl")
	private String actionUrl;
	//显示顺序
	@Column(name="ShowOrder")
	private String showOrder;
	//类别
	@Column(name="ShowType")
	private String showType;
	//图标
	@Column(name="IconFile")
	private String iconFile;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建时间
	@Column(name="CreateTime")
	private String createTime;
	//修改者
	@Column(name="UpdateUser")
	private String updateUser;
	//修改时间
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public Desktop() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getIconFile() {
		return iconFile;
	}
	public void setIconFile(String iconFile) {
		this.iconFile = iconFile;
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
