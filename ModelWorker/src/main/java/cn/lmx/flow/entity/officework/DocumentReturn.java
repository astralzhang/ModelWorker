package cn.lmx.flow.entity.officework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="DocumentReturn")
@Table(name="DocumentReturn")
public class DocumentReturn {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//公文ID
	@Column(name="ParentId")
	private String parentId;
	//上报ID
	@Column(name="SubmitId")
	private String submitId;
	//退回工会编码
	@Column(name="ReturnTradeCode")
	private String returnTradeCode;
	//接收工会编码
	@Column(name="RecvTradeCode")
	private String recvTradeCode;
	//退回日期
	@Column(name="ReturnDate")
	private String returnDate;
	//退回原因
	@Column(name="ReturnCause")
	private String returnCause;
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
	 * 构造含数
	 */
	public void DocumentReturn() {
		
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
	public String getSubmitId() {
		return submitId;
	}
	public void setSubmitId(String submitId) {
		this.submitId = submitId;
	}
	public String getReturnTradeCode() {
		return returnTradeCode;
	}
	public void setReturnTradeCode(String returnTradeCode) {
		this.returnTradeCode = returnTradeCode;
	}
	public String getRecvTradeCode() {
		return recvTradeCode;
	}
	public void setRecvTradeCode(String recvTradeCode) {
		this.recvTradeCode = recvTradeCode;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getReturnCause() {
		return returnCause;
	}
	public void setReturnCause(String returnCause) {
		this.returnCause = returnCause;
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
