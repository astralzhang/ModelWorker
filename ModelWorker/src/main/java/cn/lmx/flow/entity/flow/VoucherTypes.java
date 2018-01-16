package cn.lmx.flow.entity.flow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "VoucherTypes")
@Table(name = "VoucherTypes")
public class VoucherTypes {
	@Id
	@Column(name="No")
	private String no;
	@Column(name="Name")
	private String name;
	@Column(name="Description")
	private String description;
	@Column(name="ViewScript")
	private String viewScript;
	@Column(name="SubmitSql")
	private String submitSql;
	@Column(name="CompleteSql")
	private String completeSql;
	//提交取消处理SQL
	@Column(name="SubmitCancelSql")
	private String submitCancelSql;
	//终审取消处理SQL
	@Column(name="CompleteCancelSql")
	private String completeCancelSql;
	//用户一栏取得SQL
	@Column(name="UserSql")
	private String userSql;
	//单据一栏取得SQL
	@Column(name="VoucherListSql")
	private String voucherListSql;
	//单据一栏显示字段
	@Column(name="VoucherListField")
	private String voucherListField;
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
	public VoucherTypes() {
		
	}
	public final String getNo() {
		return no;
	}
	public final void setNo(String no) {
		this.no = no;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
	public final String getViewScript() {
		return viewScript;
	}
	public final void setViewScript(String viewScript) {
		this.viewScript = viewScript;
	}
	public String getSubmitSql() {
		return submitSql;
	}
	public void setSubmitSql(String submitSql) {
		this.submitSql = submitSql;
	}
	public String getCompleteSql() {
		return completeSql;
	}
	public void setCompleteSql(String completeSql) {
		this.completeSql = completeSql;
	}
	public String getSubmitCancelSql() {
		return submitCancelSql;
	}
	public void setSubmitCancelSql(String submitCancelSql) {
		this.submitCancelSql = submitCancelSql;
	}
	public String getCompleteCancelSql() {
		return completeCancelSql;
	}
	public void setCompleteCancelSql(String completeCancelSql) {
		this.completeCancelSql = completeCancelSql;
	}
	public String getUserSql() {
		return userSql;
	}
	public void setUserSql(String userSql) {
		this.userSql = userSql;
	}
	public String getVoucherListSql() {
		return voucherListSql;
	}
	public void setVoucherListSql(String voucherListSql) {
		this.voucherListSql = voucherListSql;
	}
	public String getVoucherListField() {
		return voucherListField;
	}
	public void setVoucherListField(String voucherListField) {
		this.voucherListField = voucherListField;
	}
	public final String getCreateUser() {
		return createUser;
	}
	public final void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public final String getCreateTime() {
		return createTime;
	}
	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public final String getUpdateUser() {
		return updateUser;
	}
	public final void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public final String getUpdateTime() {
		return updateTime;
	}
	public final void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
