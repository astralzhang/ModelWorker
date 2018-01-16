package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="UserDefineViews")
@Table(name="UserDefineViews")
public class UserDefineViews {
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
	//版本
	@Column(name="Version")
	private String version;
	//发布版本号
	@Column(name="PublishVersion")
	private String publishVersion;
	//单据类型
	@Column(name="VoucherType")
	private String voucherType;
	//状态
	@Column(name="Status")
	private String status;
	//列表脚本
	@Column(name="ListScript")
	private String listScript;
	//Head脚本
	@Column(name="HeadScript")
	private String headScript;
	//明细脚本
	@Column(name="DetailScript")
	private String detailScript;
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
	public UserDefineViews() {
		
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
	public String getPublishVersion() {
		return publishVersion;
	}
	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
