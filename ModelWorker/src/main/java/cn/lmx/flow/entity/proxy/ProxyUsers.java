package cn.lmx.flow.entity.proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="ProxyUsers")
@Table(name="ProxyUsers")
public class ProxyUsers {
	@Id
	@Column(name="ID")
	private String id;
	//代理单据类型
	@Column(name="VoucherType")
	private String voucherType;
	//元用户
	@Column(name="UserNo")
	private String userNo;
	//元用户名
	@Column(name="UserName")
	private String userName;
	//代理用户
	@Column(name="ProxyUserNo")
	private String proxyUserNo;
	//代理用户名
	@Column(name="ProxyUserName")
	private String proxyUserName;
	//起始日期
	@Column(name="StartDate")
	private String startDate;
	//结束日期
	@Column(name="EndDate")
	private String endDate;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建日期
	@Column(name="CreateTime")
	private String createTime;
	//更新者
	@Column(name="UpdateUser")
	private String updateUser;
	//更新日期
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public ProxyUsers() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public void setProxyUserNo(String proxyUserNo) {
		this.proxyUserNo = proxyUserNo;
	}
	public String getUserName() {
		return userName;
	}
	public String getProxyUserNo() {
		return proxyUserNo;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProxyUserName() {
		return proxyUserName;
	}
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
