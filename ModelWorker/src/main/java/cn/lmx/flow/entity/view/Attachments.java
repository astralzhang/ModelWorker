package cn.lmx.flow.entity.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Attachments")
@Table(name="Attachments")
public class Attachments {
	@Id
	@Column(name="ID")
	private String id;
	//画面编码
	@Column(name="ViewNo")
	private String viewNo;
	//数据ID
	@Column(name="DataId")
	private String dataId;
	//序号
	@Column(name="SeqNo")
	private String seqNo;
	//文件类型
	@Column(name="FileExtend")
	private String fileExtend;
	//文件名称
	@Column(name="FileName")
	private String fileName;
	//文件内容
	@Column(name="FileContent")
	private byte[] fileContent;
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
	public Attachments() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getViewNo() {
		return viewNo;
	}
	public void setViewNo(String viewNo) {
		this.viewNo = viewNo;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getFileExtend() {
		return fileExtend;
	}
	public void setFileExtend(String fileExtend) {
		this.fileExtend = fileExtend;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
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
