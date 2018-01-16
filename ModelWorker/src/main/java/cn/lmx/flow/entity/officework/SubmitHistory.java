package cn.lmx.flow.entity.officework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 上报履历
 * @author yujx
 *
 */
@Entity(name="SubmitHistory")
@Table(name="SubmitHistory")
public class SubmitHistory {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//公文ID
	@Column(name="ParentId")
	private String parentId;
	//上报单位编码
	@Column(name="SubmitTradeCode")
	private String submitTradeCode;
	//接收单位编码
	@Column(name="RecvTradeCode")
	private String recvTradeCode;
	//上报日期
	@Column(name="SubmitDate")
	private String submitDate;
	//上报阶层
	@Column(name="SubmitLevel")
	private String submitLevel;
	//状态
	@Column(name="Status")
	private String status;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建日期
	@Column(name="CreateTime")
	private String createTime;
	//修改者
	@Column(name="UpdateUser")
	private String updateUser;
	//修改日期
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public SubmitHistory() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getSubmitTradeCode() {
		return submitTradeCode;
	}
	public void setSubmitTradeCode(String submitTradeCode) {
		this.submitTradeCode = submitTradeCode;
	}
	public String getRecvTradeCode() {
		return recvTradeCode;
	}
	public void setRecvTradeCode(String recvTradeCode) {
		this.recvTradeCode = recvTradeCode;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getSubmitLevel() {
		return submitLevel;
	}
	public void setSubmitLevel(String submitLevel) {
		this.submitLevel = submitLevel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
