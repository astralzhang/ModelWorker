package cn.lmx.flow.entity.officework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公文附件
 * @author yujx
 *
 */
@Entity(name="DocumentAttachment")
@Table(name="DocumentAttachment")
public class DocumentAttachment {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//公文ID
	@Column(name="ParentId")
	private String parentId;
	//文件名称
	@Column(name="FileName")
	private String fileName;
	//文件类型
	@Column(name="FileType")
	private String fileType;
	//文件内容
	@Column(name="FileRealName")
	private String fileRealName;
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
	public DocumentAttachment() {
		
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
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
