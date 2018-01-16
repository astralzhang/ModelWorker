package cn.lmx.flow.entity.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ModuleItems")
@Entity(name="ModuleItems")
public class ModuleItems {
	//功能编码
	@Id
	@Column(name="ItemNo")
	private String itemNo;
	//功能名称
	@Column(name="ItemName")
	private String itemName;
	//功能描述
	@Column(name="Description")
	private String description;
	//访问URL
	@Column(name="ActionUrl")
	private String actionUrl;
	//所属模块
	@Column(name="ModuleNo")
	private String moduleNo;
	//是否显示菜单
	@Column(name="MenuFlag")
	private String menuFlag;
	//菜单显示顺序
	@Column(name="ShowOrder")
	private String showOrder;
	//Css样式
	@Column(name="CssStyle")
	private String cssStyle;
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
	public ModuleItems() {
		
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getModuleNo() {
		return moduleNo;
	}
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}
	public String getMenuFlag() {
		return menuFlag;
	}
	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	public String getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}
	public String getCssStyle() {
		return cssStyle;
	}
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
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
