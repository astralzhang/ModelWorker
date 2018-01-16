package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="ReportTemplate")
@Entity(name="ReportTemplate")
public class ReportTemplate {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//报表编码
	@Column(name="ReportNo")
	private String reportNo;
	//模板文件名称
	@Column(name="FileName")
	private String fileName;
	//模板文件
	@Column(name="Template")
	private byte[] template;
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
	public ReportTemplate() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getTemplate() {
		return template;
	}
	public void setTemplate(byte[] template) {
		this.template = template;
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
