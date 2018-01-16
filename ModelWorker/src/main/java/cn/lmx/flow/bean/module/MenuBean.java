package cn.lmx.flow.bean.module;

import java.util.ArrayList;
import java.util.List;

public class MenuBean {
	//模块编码
	private String no;
	//模块名称
	private String name;
	//显示顺序
	private String showOrder;
	//Css样式
	private String cssStyle;
	//功能一栏
	private List<ModuleItemBean> itemList;
	/**
	 * 构造函数
	 */
	public MenuBean() {
		itemList = new ArrayList<ModuleItemBean>();
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
	public List<ModuleItemBean> getItemList() {
		return itemList;
	}
	public void setItemList(List<ModuleItemBean> itemList) {
		this.itemList = itemList;
	}
	public void addItem(ModuleItemBean bean) {
		if (itemList == null) {
			itemList = new ArrayList<ModuleItemBean>();
		}
		for (int i = 0; i < itemList.size(); i++) {
			ModuleItemBean itemBean = itemList.get(i);
			if (bean.getShowOrder().compareTo(itemBean.getShowOrder()) < 0) {
				itemList.add(i, bean);
				return;
			}
		}
		itemList.add(bean);
	}
}
