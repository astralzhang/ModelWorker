package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="PublishedViews")
@Table(name="PublishedViews")
public class PublishedViews {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//编码
	@Column(name="No")
	private String no;
	//名称
	@Column(name="Name")
	private String name;
	//版本号
	@Column(name="Version")
	private String version;
	//单据类型
	@Column(name="VoucherType")
	private String voucherType;
	//列表脚本
	@Column(name="ListScript")
	private String listScript;
	//Head脚本
	@Column(name="HeadScript")
	private String headScript;
	//明细脚本
	@Column(name="DetailScript")
	private String detailScript;
	//所属模块
	@Column(name="ModuleNo")
	private String moduleNo;
	//所在位置
	@Column(name="ShowOrder")
	private String showOrder;
	@Column(name="CreateUser")
	private String createUser;
	@Column(name="CreateTime")
	private String createTime;
	@Column(name="UpdateUser")
	private String updateUser;
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public PublishedViews() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getListScript() {
		return listScript;
	}
	public void setListScript(String listScript) {
		this.listScript = listScript;
	}
	public String getHeadScript() {
		return headScript;
	}
	public void setHeadScript(String headScript) {
		this.headScript = headScript;
	}
	public String getDetailScript() {
		return detailScript;
	}
	public void setDetailScript(String detailScript) {
		this.detailScript = detailScript;
	}
	public String getModuleNo() {
		return moduleNo;
	}
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}
	public String getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
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
