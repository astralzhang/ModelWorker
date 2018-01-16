package cn.lmx.flow.bean.officework;

public class DocumentAttachmentBean {
	//ID
	private String id;
	//公文ID
	private String parentId;
	//文件名称
	private String fileName;
	//文件类型
	private String fileType;
	//文件内容
	private byte[] fileContent;
	//文件保存名
	private String fileRealName;
	//上传时间
	private String uploadTime;
	/**
	 * 构造函数
	 */
	public DocumentAttachmentBean() {
		
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
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
}
