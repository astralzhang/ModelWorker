package cn.lmx.flow.entity.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="Modules")
@Entity(name="Modules")
public class Module {
	//模块编码
	@Id
	@Column(name="NO")
	private String no;
	//模块名称
	@Column(name="Name")
	private String name;
	//访问URL
	@Column(name="URL")
	private String url;
	//模块类型
	@Column(name="Type")
	private String type;
	//父模块
	@Column(name="Parent")
	private String parent;
	//菜单显示顺序
	@Column(name="ShowOrder")
	private String shorwOrder;
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
	public Module() {
		
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getShorwOrder() {
		return shorwOrder;
	}
	public void setShorwOrder(String shorwOrder) {
		this.shorwOrder = shorwOrder;
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
