package cn.lmx.flow.bean.view;

public class AttachmentBean {
	private String id;
	//画面编码
	private String viewNo;
	//数据ID
	private String dataId;
	//序号
	private String seqNo;
	//文件类型
	private String fileExtend;
	//文件名称
	private String fileName;
	//文件内容
	private byte[] fileContent;
	/**
	 * 构造函数
	 */
	public AttachmentBean() {
		
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
}
