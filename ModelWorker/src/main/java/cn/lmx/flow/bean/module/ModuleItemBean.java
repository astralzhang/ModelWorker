package cn.lmx.flow.bean.module;

public class ModuleItemBean {
	//功能编号
	private String itemNo;
	//功能名称
	private String itemName;
	//功能描述
	private String description;
	//访问URL
	private String actionUrl;
	//所属模块
	private String moduleNo;
	//模块名称
	private String moduleName;
	//是否显示菜单
	private String menuFlag;
	//显示顺序
	private String showOrder;
	//Css样式
	private String cssStyle;
	/**
	 * 构造函数
	 */
	public ModuleItemBean() {
		
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
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
